package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.SubmissionDTO;
import hu.indicium.speurtocht.domain.FileSubmission;
import hu.indicium.speurtocht.domain.Submission;
import hu.indicium.speurtocht.service.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
public class SubmissionController {

	private SubmissionService service;

	@GetMapping
	public List<SubmissionDTO> getPending() {
		return this.service.getPending()
				.stream()
				.map((submission -> new SubmissionDTO(submission.getId(), submission.getTeam().getName(), submission.getClass().getSimpleName())))
				.toList();
	}

	@PatchMapping("/{id}/approve")
	public void approve(@PathVariable UUID id) {
		this.service.approve(id);
	}

	@PatchMapping("/{id}/deny")
	public void deny(@PathVariable UUID id) {
		this.service.deny(id);
	}

	@GetMapping("/{id}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable UUID id) {
		HttpHeaders responseHeaders = new HttpHeaders();
		FileSubmission file = this.service.getFile(id);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}
}
