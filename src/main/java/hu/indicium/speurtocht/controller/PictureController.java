package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.PictureSubmissionDTO;
import hu.indicium.speurtocht.controller.dto.SubmissionDTO;
import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Tag(
		name = "Picture Bingo",
		description = "Search through a pre-defined area for these locations."
)
@RestController
@RequestMapping("/pictures")
@AllArgsConstructor
public class PictureController {

	@Autowired
	private AuthUtils authUtils;

	private PictureService pictureService;
	private TeamService teamService;

	@Operation(summary = "Upload a picture for a new location")
	@PostMapping
	public void createPictures(@RequestParam("files") MultipartFile[] files) throws IOException {
		// todo limit to 25 pictures
		for (MultipartFile file : files) {
			this.pictureService.createPictures(new Coordinate(0f, 0f), file);
		}
	}

//	@Operation(summary = "Get a list of picture id's")
//	@GetMapping
//	public List<Long> pictures() {
//		return this.pictureService.getAll().stream().map(Picture::getId).toList();
//	}

	@Operation(summary = "Get image file of a particular location")
	@GetMapping("/{id}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable Long id) {
		HttpHeaders responseHeaders = new HttpHeaders();
		PictureFile file = this.pictureService.getFile(id);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}

	@Operation(
			summary = "Get my team's picture submissions",
			description = "Get my team's picture submissions grouped by the picture id it was submitted to."
	)
	@GetMapping
	public Collection<PictureSubmissionDTO> getTeamPictures() {
		return this.pictureService.getTeamsPictures(authUtils.getTeam()).values();
	}

	@Operation(summary = "Create submission for a location")
	@ApiResponses({
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "Failed to parse image."),
			@ApiResponse(responseCode = "403", description = "There's already a image submitted for this location.")
	})
	@PostMapping(value = "/{pictureId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void createSubmission(@PathVariable Long pictureId, @RequestParam("file") MultipartFile file) {
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
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	@Operation(summary = "Get list of picture submissions awaiting approval")
	@GetMapping("/pending")
	public List<SubmissionDTO> getPending() {
		return this.pictureService.getPending()
				.stream()
				.map((submission -> new SubmissionDTO(submission.getPicture().getId(), submission.getTeam().getName(), submission.getTeam().getId())))
				.toList();
	}

//	@Operation(summary = "Get data about a picture submission")
//	@GetMapping("/{pictureId}/teams/{teamId}")
//	public PictureSubmissionDTO getSubmission(@PathVariable Long pictureId, @PathVariable UUID teamId) {
//		PictureSubmission submission = this.pictureService.getSubmission(this.teamService.getTeam(teamId), pictureId);
//		return new PictureSubmissionDTO(submission.getTeam().getName(), submission.getPicture().getId());
//	}

	@Operation(summary = "Approve a picture submission")
	@PatchMapping("/{pictureId}/teams/{teamId}/approve")
	public void approve(@PathVariable Long pictureId, @PathVariable UUID teamId) {
		this.pictureService.approve(this.teamService.getTeam(teamId), pictureId);
	}

	@Operation(summary = "Deny a picture submission")
	@PatchMapping("/{pictureId}/teams/{teamId}/deny")
	public void deny(@PathVariable Long pictureId, @PathVariable UUID teamId) {
		this.pictureService.deny(this.teamService.getTeam(teamId), pictureId);
	}

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
