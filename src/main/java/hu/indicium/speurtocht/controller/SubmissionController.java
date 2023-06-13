package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.domain.FileSubmission;
import hu.indicium.speurtocht.service.SubmissionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
public class SubmissionController {

	private SubmissionService service;

	@PatchMapping("/{id}/approve")
	public void approve(@PathVariable Long id) {
		this.service.approve(id);
	}

	@PatchMapping("/{id}/deny")
	public void deny(@PathVariable Long id) {
		this.service.deny(id);
	}

	@GetMapping("/{id}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable Long id) {
		HttpHeaders responseHeaders = new HttpHeaders();


		FileSubmission file = this.service.getFile(id);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}
}
