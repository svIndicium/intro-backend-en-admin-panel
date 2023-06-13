package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.FileSubmission;
import hu.indicium.speurtocht.domain.Submission;
import hu.indicium.speurtocht.domain.SubmissionState;
import hu.indicium.speurtocht.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubmissionService {
	private SubmissionRepository repository;

	public List<Submission> getPending() {
		return this.repository.findByStatus(SubmissionState.PENDING);
	}

	public void approve(UUID id) {
		Submission submission = this.repository.getReferenceById(id);
		submission.approve();
		this.repository.save(submission);
	}

	public void deny(UUID id) {
		Submission submission = this.repository.getReferenceById(id);
		submission.deny();
		this.repository.save(submission);
	}

	public FileSubmission getFile(UUID id) {
		return this.repository.getReferenceById(id).getFileSubmission();
	}
}
