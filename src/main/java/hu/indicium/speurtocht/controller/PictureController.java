package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.PictureSubmissionDTO;
import hu.indicium.speurtocht.controller.dto.SubmissionDTO;
import hu.indicium.speurtocht.controller.dto.SubmissionDeniedDTO;
import hu.indicium.speurtocht.domain.Coordinate;
import hu.indicium.speurtocht.domain.FileSubmission;
import hu.indicium.speurtocht.domain.PictureFile;
import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.PictureService;
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
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Tag(
		name = "Picture Bingo",
		description = "Search through a pre-defined area for these locations."
)
@RestController
@SecurityRequirement(name = "speurtocht-88")
@RequestMapping("/pictures")
@AllArgsConstructor
public class PictureController {

	@Autowired
	private AuthUtils authUtils;

	private PictureService pictureService;
	private TeamService teamService;

	@Secured("ADMIN")
	@Operation(summary = "Upload a picture for a new location")
	@PostMapping
	public void createPictures(@RequestParam("files") MultipartFile[] files) throws IOException {
		// todo limit to 25 pictures
		for (MultipartFile file : files) {
			this.pictureService.createPictures(new Coordinate(0f, 0f), file);
		}
	}

	@Operation(summary = "Get image file of a particular location")
	@GetMapping("/{pictureId}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable Long pictureId) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setCacheControl(CacheControl.maxAge(Duration.of(2, ChronoUnit.HOURS)));
		PictureFile file = this.pictureService.getFile(pictureId);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}

	@Operation(summary = "Get image file of team's submission")
	@GetMapping("/{pictureId}/submission")
	@Transactional
	public ResponseEntity<byte[]> getSubmissionContent(@PathVariable Long pictureId) {
		Team team = this.authUtils.getTeam();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setCacheControl(CacheControl.maxAge(Duration.of(2, ChronoUnit.HOURS)));
		FileSubmission file = this.pictureService.getSubmissionFile(team, pictureId);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}

	@Operation(summary = "Get thumbnail of a particular location")
	@GetMapping("/{pictureId}/thumbnail")
	@Transactional
	public ResponseEntity<byte[]> getThumbnail(@PathVariable Long pictureId) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setCacheControl(CacheControl.maxAge(Duration.of(2, ChronoUnit.HOURS)));
		PictureFile file = this.pictureService.getThumbnail(pictureId);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}

	@Operation(summary = "Get list of pictures")
	@GetMapping
	public Collection<PictureSubmissionDTO> getPictures() {
		return this.pictureService.getTeamsPictures(this.authUtils.getTeam()).values();
	}

	@Secured("PARTICIPANT")
	@Operation(summary = "Create submission for a location")
	@ApiResponses({
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "Failed to parse image."),
			@ApiResponse(responseCode = "403", description = "There's already a image submitted for this location.")
	})
	@PostMapping(value = "/{pictureId}")
	public void createSubmission(@PathVariable Long pictureId, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file is empty");
		try {
			this.pictureService.createSubmission(
					this.authUtils.getTeam(),
					this.pictureService.getPicture(pictureId),
					file
			);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.LOCKED);
		}
	}

	@Secured("ADMIN")
	@Operation(summary = "Get list of picture submissions awaiting approval")
	@GetMapping("/pending")
	public List<SubmissionDTO> getPending() {
		return this.pictureService.getPending()
				.stream()
				.map((submission -> new SubmissionDTO(submission.getPicture().getId(), submission.getTeam().getName(), submission.getTeam().getId())))
				.toList();
	}

	@Secured("ADMIN")
	@Operation(summary = "Approve a picture submission")
	@PatchMapping("/{pictureId}/teams/{teamId}/approve")
	public void approve(@PathVariable Long pictureId, @PathVariable UUID teamId) {
		this.pictureService.approve(this.teamService.getTeam(teamId), pictureId);
	}

	@Secured("ADMIN")
	@Operation(summary = "Deny a picture submission")
	@PatchMapping("/{pictureId}/teams/{teamId}/deny")
	public void deny(@PathVariable Long pictureId, @PathVariable UUID teamId, @RequestBody SubmissionDeniedDTO dto) {
		this.pictureService.deny(this.teamService.getTeam(teamId), pictureId, dto.deniedReason());
	}

	@Secured("ADMIN")
	@Operation(summary = "Get a submission's submitted image")
	@GetMapping("/{pictureId}/teams/{teamId}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable Long pictureId, @PathVariable UUID teamId) {
		HttpHeaders responseHeaders = new HttpHeaders();
		FileSubmission file = this.pictureService.getSubmissionFile(this.teamService.getTeam(teamId), pictureId);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}
}
