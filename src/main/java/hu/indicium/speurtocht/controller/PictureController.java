package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.PictureSubmissionDTO;
import hu.indicium.speurtocht.controller.dto.SubmissionDTO;
import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pictures")
@AllArgsConstructor
public class PictureController {

	@Autowired
	private AuthUtils authUtils;

	private PictureService pictureService;

	@PostMapping
	public void createPictures(@RequestParam("files") MultipartFile[] files) throws IOException {
		for (MultipartFile file : files) {
			this.pictureService.createPictures(new Coordinate(0f, 0f), file);
		}
	}

	@GetMapping
	public List<Long> pictures() {
		return this.pictureService.getAll().stream().map(Picture::getId).toList();
	}

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

	@GetMapping("/team")
	public Map<Long, List<SubmissionState>> getTeamPictures() {
		return this.pictureService.getTeamsPictures(authUtils.getTeam());
	}

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
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/submissions")
	public List<SubmissionDTO> getPending() {
		return this.pictureService.getPending()
				.stream()
				.map((submission -> new SubmissionDTO(submission.getId(), submission.getTeam().getName(), submission.getClass().getSimpleName())))
				.toList();
	}

	@GetMapping("/submissions/{id}")
	public PictureSubmissionDTO getPending(@PathVariable UUID id) {
		PictureSubmission submission = this.pictureService.getSubmission(id);
		return new PictureSubmissionDTO(submission.getTeam().getName(), submission.getPicture().getId());
	}

	@PatchMapping("/submissions/{id}/approve")
	public void approve(@PathVariable UUID id) {
		this.pictureService.approve(id);
	}

	@PatchMapping("/submissions/{id}/deny")
	public void deny(@PathVariable UUID id) {
		this.pictureService.deny(id);
	}

	@GetMapping("/submissions/{id}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable UUID id) {
		HttpHeaders responseHeaders = new HttpHeaders();
		FileSubmission file = this.pictureService.getSubmissionFile(id);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}
}
