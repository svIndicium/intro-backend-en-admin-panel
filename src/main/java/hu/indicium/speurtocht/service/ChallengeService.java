package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.repository.ChallengeRepository;
import hu.indicium.speurtocht.repository.ChallengeSubmissionRepository;
import hu.indicium.speurtocht.repository.FileSubmissionRepository;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChallengeService {

	private ChallengeRepository repository;
	private ChallengeSubmissionRepository submissionRepository;
	private FileSubmissionRepository fileSubmissionRepository;

	public Challenge save(String title, String challenge, int points) {
		return this.repository.save(new Challenge(title, challenge, points));
	}

	public List<Challenge> createBulk(List<Challenge> challenges) {
		return this.repository.saveAll(challenges);
	}

	public Challenge getChallenge(Long id) {
		return this.repository.getReferenceById(id);
	}

	public ChallengeSubmission getSubmission(UUID id) {
		return this.submissionRepository.getReferenceById(id);
	}

	public long getTeamPoints(Team team) {
		return this.submissionRepository.findByTeamAndStatus(team, SubmissionState.APPROVED).stream().map(e -> e.getChallenge().getPoints()).reduce(0, Integer::sum);
	}

	public ChallengeSubmission createSubmission(Team team, Challenge challenge, MultipartFile[] files) throws IOException, AlreadyApprovedException {
		if (this.submissionRepository.existsByTeamAndChallengeAndStatus(team, challenge, SubmissionState.APPROVED)) throw new AlreadyApprovedException();
		return this.submissionRepository.save(new ChallengeSubmission(team, challenge, files));
	}

	public List<Challenge> getAll() {
		return this.repository.findAll();
	}

	@Transactional
	public HashMap<Long, List<SubmissionState>> getTeamChallenges(Team team) {
		List<Challenge> challenges = this.repository.findAll();
		HashMap<Long, List<SubmissionState>> output = new HashMap<>(challenges.size());
		List<ChallengeSubmission> submissions = this.submissionRepository.findByTeam(team);
		for (Challenge challenge : challenges) {
			output.put(
					challenge.getId(),
					submissions
							.stream()
							.filter(
									(submission) -> submission.getChallenge().getId() == challenge.getId())
							.map(ChallengeSubmission::getStatus)
							.toList()
			);
		}

		return output;
	}

	public List<ChallengeSubmission> getPending() {
		return this.submissionRepository.findByStatus(SubmissionState.PENDING);
	}

	public void approve(UUID id) {
		ChallengeSubmission submission = this.submissionRepository.getReferenceById(id);
		submission.approve();
		this.submissionRepository.save(submission);
	}

	public void deny(UUID id) {
		ChallengeSubmission submission = this.submissionRepository.getReferenceById(id);
		submission.deny();
		this.submissionRepository.save(submission);
	}

	public FileSubmission getSubmissionFile(UUID id) {
		return this.fileSubmissionRepository.getReferenceById(id);
	}
}
