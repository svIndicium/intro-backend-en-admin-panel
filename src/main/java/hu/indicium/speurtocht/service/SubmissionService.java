package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.FileSubmission;
import hu.indicium.speurtocht.domain.Submission;
import hu.indicium.speurtocht.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubmissionService {
	private SubmissionRepository repository;

	public void approve(Long id) {
		Submission submission = this.repository.getReferenceById(id);
		submission.approve();
		this.repository.save(submission);
	}

	public void deny(Long id) {
		Submission submission = this.repository.getReferenceById(id);
		submission.deny();
		this.repository.save(submission);
	}

	public FileSubmission getFile(Long id) {
		return this.repository.getReferenceById(id).getFileSubmission();
	}
}
