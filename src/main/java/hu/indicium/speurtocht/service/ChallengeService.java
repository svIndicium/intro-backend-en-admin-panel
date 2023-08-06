package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.controller.dto.ChallengeStatusDTO;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

	public ChallengeSubmission getSubmission(Team team, Long id) {
		return this.submissionRepository.getReferenceById(new ChallengeSubmissionId(team, this.getChallenge(id)));
	}

	public long getTeamPoints(Team team) {
		return this.submissionRepository.findByTeamAndStatus(team, SubmissionState.APPROVED).stream().map(e -> e.getChallenge().getPoints()).reduce(0, Integer::sum);
	}

	public ChallengeSubmission createSubmission(Team team, Challenge challenge, MultipartFile[] files) throws IOException, AlreadyApprovedException {
		if (this.submissionRepository.existsByTeamAndChallengeAndStatusIn(team, challenge, List.of(SubmissionState.PENDING, SubmissionState.APPROVED))) throw new AlreadyApprovedException();
		return this.submissionRepository.save(new ChallengeSubmission(team, challenge, files));
	}

	public List<Challenge> getAll() {
		return this.repository.findAll();
	}

	@Transactional
	public Map<Long, ChallengeStatusDTO> getTeamChallenges(Team team) {
		Map<Long, ChallengeStatusDTO> collected = this.repository
				.findAll()
				.stream()
				.collect(
						Collectors.toMap(
								Challenge::getId,
								(challenge) -> new ChallengeStatusDTO(
										challenge.getId(),
										challenge.getTitle(),
										challenge.getChallenge(),
										challenge.getPoints(),
										null,
										null
								)
						)
				);

		Map<Long, ChallengeStatusDTO> submitted = this.submissionRepository
				.findByTeam(team)
				.stream()
				.collect(
						Collectors.toMap(
								(e) -> e.getChallenge().getId(),
								(submission) -> new ChallengeStatusDTO(
										submission.getChallenge().getId(),
										submission.getChallenge().getTitle(),
										submission.getChallenge().getChallenge(),
										submission.getChallenge().getPoints(),
										submission.getStatus(),
										submission.getSubmittedAt()
								)
						)
				);

		collected.putAll(submitted);

		return collected;
	}

	public List<ChallengeSubmission> getPending() {
		return this.submissionRepository.findByStatus(SubmissionState.PENDING);
	}

	public void approve(Team team, Long id) {
		ChallengeSubmission submission = this.submissionRepository.getReferenceById(new ChallengeSubmissionId(team, this.getChallenge(id)));
		submission.approve();
		this.submissionRepository.save(submission);
	}

	public void deny(Team team, Long id) {
		ChallengeSubmission submission = this.submissionRepository.getReferenceById(new ChallengeSubmissionId(team, this.getChallenge(id)));
		submission.deny();
		this.submissionRepository.save(submission);
	}

	public FileSubmission getSubmissionFile(UUID id) {
		return this.fileSubmissionRepository.getReferenceById(id);
	}
}
