package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.*;
import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.FileStorageService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Tag(
		name = "Crazy 88",
		description = "Define 88 challenges that teams can complete. Teams compete against each other. The winner is the team that scores the most points."
)
@RestController
@SecurityRequirement(name = "speurtocht-88")
@RequestMapping("/challenges")
@AllArgsConstructor
public class ChallengeController {

	@Autowired
	private AuthUtils authUtils;

	private ChallengeService challengeService;
	private FileStorageService fileStorageService;
	private TeamService teamService;

	@Secured("ADMIN")
	@Operation(summary = "Create challenges")
	@PostMapping
	public void createChallenges(@RequestBody List<CreateChallengeDTO> challenges) {
		// todo maximum of 88 challenges
		this.challengeService.createBulk(challenges.stream().map(challenge -> new Challenge(challenge.title(), challenge.challenge(), challenge.points())).toList());
	}

	@Operation(summary = "Get list of challenges")
	@GetMapping
	public Collection<ChallengeStatusDTO> getChallenges() {
		return this.challengeService.getTeamChallenges(this.authUtils.getTeam())
				.values()
				.stream()
				.sorted(new ChallengeComparator())
				.toList();
	}

	private static class ChallengeComparator implements Comparator<ChallengeStatusDTO> {

		private static final List<SubmissionState> STATE_SET = Arrays.asList(SubmissionState.DENIED, null, SubmissionState.PENDING, SubmissionState.APPROVED);

		@Override
		public int compare(ChallengeStatusDTO o1, ChallengeStatusDTO o2) {
			int i1 = STATE_SET.indexOf(o1.state());
			int i2 = STATE_SET.indexOf(o2.state());
			if (i1 == i2) {
				if (o1.state() != null) {
					return compareTimes(o1.submittedAt(), o2.submittedAt());
				} else {
					return compareIds(o1.id(), o2.id());
				}
			} else {
				return comparePosition(i1, i2);
			}
		}

		private int compareIds(Long o1, Long o2) {
			return (int) (o1 - o2);
		}

		private int compareTimes(Instant o1, Instant o2) {
			if (o1.isAfter(o2)) {
				return -1;
			} else if (o1.isBefore(o2)) {
				return 1;
			} else {
				return 0;
			}
		}

		private int comparePosition(int p1, int p2) {
			if (p1 == p2) {
				return 0;
			} else {
				return p1 - p2;
			}
		}
	}

	@Secured("PARTICIPANT")
	@Operation(summary = "Create submission for a challenge")
	@ApiResponses({
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "Failed to parse image."),
			@ApiResponse(responseCode = "423", description = "There's already a image submitted for this challenge.")
	})
	@PostMapping(value = "/{challengeId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void createSubmission(@PathVariable Long challengeId, @RequestParam("files") MultipartFile[] files) {
		try {
			this.challengeService.createSubmission(
					this.authUtils.getTeam(),
					this.challengeService.getChallenge(challengeId),
					files
			);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.LOCKED);
		}
	}

	@Secured("PARTICIPANT")
	@Operation(summary = "Create submission for a challenge")
	@ApiResponses({
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "423", description = "There's already a image submitted for this challenge.")
	})
	@PostMapping(value = "/{challengeId}/attempt")
	public Long createSubmissionAttempt(
			@PathVariable Long challengeId,
			@RequestBody Map<String, Integer> files
	) {
		try {
			SubmissionAttempt attempt = this.challengeService.createAttempt(
					this.authUtils.getTeam(),
					this.challengeService.getChallenge(challengeId),
					files
			);
			return attempt.getId();
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.LOCKED);
		}
//		try {
//			this.challengeService.createSubmission(
//					this.authUtils.getTeam(),
//					this.challengeService.getChallenge(challengeId),
//					files
//			);
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//		} catch (AlreadyApprovedException e) {
//			throw new ResponseStatusException(HttpStatus.LOCKED);
//		}
	}

	@Secured("PARTICIPANT")
	@Operation(summary = "Create submission for a challenge")
	@ApiResponses({
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "Failed to parse image."),
			@ApiResponse(responseCode = "423", description = "There's already a image submitted for this challenge.")
	})
	@PostMapping(value = "/{challengeId}/attempt/{attemptId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void createSubmissionChunked(
			@PathVariable Long challengeId,
			@PathVariable Long attemptId,
			@RequestParam("chunk") MultipartFile chunk,
			@RequestParam("fileName") String fileName,
			@RequestParam("fileType") String fileType,
			@RequestParam("chunkIndex") int chunkIndex
//			@RequestParam("totalChunks") int totalChunks
	) {
		try {
			Team team = this.authUtils.getTeam();
			Challenge challenge = this.challengeService.getChallenge(challengeId);
			this.challengeService.addChunk(
					team,
					challenge,
					attemptId,
                    (long) chunkIndex,
					fileName,
					fileType,
					chunk

			);

			this.challengeService.processSubmission(team, challenge, attemptId);

		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.LOCKED);
		}
	}

	@Secured("ADMIN")
	@Operation(summary = "Get list of Crazy 88 submissions awaiting approval")
	@GetMapping("/pending")
	public List<PendingChallengeSubmissionDTO> getPending() {
		return this.challengeService
				.getPending()
				.stream()
				.map(
						submission -> new PendingChallengeSubmissionDTO(submission.getChallenge().getId(), submission.getChallenge().getTitle(), submission.getTeam().getName(), submission.getTeam().getId())
				)
				.toList();
	}

	@Secured("ADMIN")
	@Operation(summary = "Get data about a Crazy 88 submission")
	@GetMapping("/{challengeId}/teams/{teamId}")
	@Transactional
	public ChallengeSubmissionDTO getSubmission(@PathVariable Long challengeId, @PathVariable UUID teamId) {
		return this.challengeService.getSubmissionOptimized(teamId, challengeId);
//		return new ChallengeSubmissionDTO(
//				submission.getChallenge().getTitle(),
//				submission.getChallenge().getChallenge(),
//				submission.getChallenge().getPoints(),
//				submission.getTeam().getName(),
//				submission.getFileSubmission().stream()
//						.map(FileSubmission::getId)
//						.toList()
//		);
	}

	@Secured("ADMIN")
	@Operation(summary = "Approve a Crazy 88 submission")
	@PatchMapping("/{challengeId}/teams/{teamId}/approve")
	public void approve(@PathVariable Long challengeId, @PathVariable UUID teamId) {
		this.challengeService.approve(this.teamService.getTeam(teamId), challengeId);
	}

	@Secured("ADMIN")
	@Operation(summary = "Deny a Crazy 88 submission")
	@PatchMapping("/{challengeId}/teams/{teamId}/deny")
	public void deny(@PathVariable Long challengeId, @PathVariable UUID teamId, @RequestBody SubmissionDeniedDTO dto) {
		this.challengeService.deny(this.teamService.getTeam(teamId), challengeId, dto.deniedReason());
	}

//	@Secured("ADMIN")
//	@Operation(summary = "Get a file from a Crazy 88 submission")
//	@GetMapping("/submissions/{fileId}/file")
//	@Transactional
//	public ResponseEntity<byte[]> getContent(@PathVariable UUID fileId) {
//		HttpHeaders responseHeaders = new HttpHeaders();
//		FileSubmission file = this.challengeService.getSubmissionFile(fileId);
//		responseHeaders.set("Content-Type", file.getType());
//		return ResponseEntity
//				.ok()
//				.headers(responseHeaders)
//				.body(file.getContent());
//	}

//	@Secured("ADMIN")
//	@Operation(summary = "Get a file from a Crazy 88 submission")
//	@GetMapping("/submissions/{fileId}/file")
//	@Transactional
//	public ResponseEntity<Resource> getContent(@PathVariable UUID fileId) {
//		FileSubmission file = this.challengeService.getSubmissionFile(fileId);
//
//		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getContent()));
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_TYPE, file.getType())
//				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getId().toString() + "\"")
//				.body(resource);
//	}

//	@Secured("ADMIN")
//	@Operation(summary = "Get a file from a Crazy 88 submission")
//	@GetMapping("/submissions/{fileId}/file")
//	@Transactional
//	public ResponseEntity<Resource> getContent(@PathVariable UUID fileId, @RequestHeader(value = "Range", required = false) String rangeHeader) {
//		FileSubmission file = this.challengeService.getSubmissionFile(fileId);
//		byte[] fileContent = file.getContent();
//
//		if (rangeHeader != null) {
//			long fileSize = fileContent.length;
//			String[] ranges = rangeHeader.replace("bytes=", "").split("-");
//			long start = Long.parseLong(ranges[0]);
//			long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileSize - 1;
//
//			if (end >= fileSize) {
//				end = fileSize - 1;
//			}
//
//			long contentLength = end - start + 1;
//			ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent, (int) start, (int) contentLength);
//			InputStreamResource resource = new InputStreamResource(inputStream);
//
//			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//					.header(HttpHeaders.CONTENT_TYPE, file.getType())
//					.header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
//					.header(HttpHeaders.ACCEPT_RANGES, "bytes")
//					.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
//					.body(resource);
//		}
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_TYPE, file.getType())
//				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getId().toString() + "\"")
//				.body(new InputStreamResource(new ByteArrayInputStream(fileContent)));
//	}


//	@Secured("ADMIN")
//	@Operation(summary = "Stream a file with authentication")
//	@GetMapping("/submissions/{fileId}/file")
//	@Transactional
//	public ResponseEntity<Resource> streamContent(
//			@PathVariable UUID fileId,
//			@RequestHeader(value = "Range", required = false) String rangeHeader) {
//
//		FileSubmission file = this.challengeService.getSubmissionFile(fileId);
//		byte[] content = file.getContent();
//		long fileSize = content.length;
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType(file.getType()));
//
//		if (rangeHeader == null) {
//			return ResponseEntity.ok()
//					.headers(headers)
//					.body(new ByteArrayResource(content));
//		}
//
//		// Parse "Range" header (e.g., "bytes=5000-")
//		String[] range = rangeHeader.replace("bytes=", "").split("-");
//		long start = Long.parseLong(range[0]);
//		long end = (range.length > 1) ? Long.parseLong(range[1]) : fileSize - 1;
//		long contentLength = end - start + 1;
//
//		headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
//		headers.setContentLength(contentLength);
//
//		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//				.headers(headers)
//				.body(new ByteArrayResource(Arrays.copyOfRange(content, (int) start, (int) (end + 1))));
//	}

	@Secured("ADMIN")
	@Operation(summary = "Get a file from a Crazy 88 submission")
	@GetMapping("/submissions/{fileId}/file")
	@Transactional
	public ResponseEntity<InputStreamResource> getContent(
			@PathVariable UUID fileId,
			@RequestHeader(value = "Range", required = false) String rangeHeader
	) throws IOException {
		// Retrieve the file from the database
		FileSubmission file = this.fileStorageService.getSubmissionFile(fileId);

		try {
			long fileSize = this.fileStorageService.getFileSize(fileId.toString());

			// If no range is specified, return the full file
			if (rangeHeader == null) {
				InputStream inputStream = this.fileStorageService.getFileOptimized(fileId.toString());
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(file.getType()));
				headers.setContentLength(fileSize);
				return ResponseEntity.ok()
						.headers(headers)
						.body(new InputStreamResource(inputStream));
			}

			// Extract the range values from the header (bytes=0-500)
			String[] ranges = rangeHeader.replace("bytes=", "").split("-");
			long start = Long.parseLong(ranges[0]);
			long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileSize - 1;

			// Adjust the end value if it's greater than the file size
			if (end >= fileSize) {
				end = fileSize - 1;
			}

			long contentLength = end - start + 1;
//			long contentLength = end - start;

			// Get the InputStream for the requested byte range
			InputStream inputStream = this.fileStorageService.getFileOptimized(fileId.toString());
			inputStream.skip(start); // Skip to the starting byte

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", file.getType());
			responseHeaders.set("Content-Length", String.valueOf(contentLength));
			responseHeaders.set("Accept-Ranges", "bytes");
			responseHeaders.set("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);

			// Return the partial content response
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
					.headers(responseHeaders)
					.body(new InputStreamResource(inputStream));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}




}
