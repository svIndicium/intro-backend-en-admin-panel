package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.controller.dto.ChallengeStatusDTO;
import hu.indicium.speurtocht.controller.dto.ChallengeSubmissionDTO;
import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.repository.*;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
	private SubmissionAttemptRepository attemptRepository;
	private SubmissionChunkRepository chunkRepository;

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

	public ChallengeSubmissionDTO getSubmissionOptimized(UUID team, Long id) {
		List<Object[]> result = this.submissionRepository.getSubmissionRaw(id, team);

		return result.stream()
				.map(row -> {
					String title = (String) row[0];
					String challenge = (String) row[1];
					int points = (Integer) row[2];
					String submittedBy = (String) row[3];
					UUID[] fileArray = (UUID[]) row[4]; // Cast the array to UUID array

					// Convert the array to a List<UUID>
					List<UUID> files = Arrays.asList(fileArray);

					return new ChallengeSubmissionDTO(title, challenge, points, submittedBy, files);
				})
//				.toList()
//				.stream()
				.findFirst()
				.get();
	}

	public long getTeamPoints(Team team) {
		return this.submissionRepository.findByTeamAndStatus(team, SubmissionState.APPROVED).stream().map(e -> e.getChallenge().getPoints()).reduce(0, Integer::sum);
	}

	public ChallengeSubmission createSubmission(Team team, Challenge challenge, MultipartFile[] files) throws IOException, AlreadyApprovedException {
		if (this.submissionRepository.existsByTeamAndChallengeAndStatusIn(team, challenge, List.of(SubmissionState.PENDING, SubmissionState.APPROVED))) throw new AlreadyApprovedException();
		return this.submissionRepository.save(new ChallengeSubmission(team, challenge, files));
	}

	public ChallengeSubmission createSubmission(Team team, Challenge challenge, List<FileSubmission> files) throws IOException, AlreadyApprovedException {
		if (this.submissionRepository.existsByTeamAndChallengeAndStatusIn(team, challenge, List.of(SubmissionState.PENDING, SubmissionState.APPROVED))) throw new AlreadyApprovedException();
		return this.submissionRepository.save(new ChallengeSubmission(team, challenge, files));
	}

	public SubmissionAttempt createAttempt(Team team, Challenge challenge, Map<String, Integer> files) throws AlreadyApprovedException {
		if (this.submissionRepository.existsByTeamAndChallengeAndStatusIn(team, challenge, List.of(SubmissionState.PENDING, SubmissionState.APPROVED))) throw new AlreadyApprovedException();
		return this.attemptRepository.save(new SubmissionAttempt(team, challenge, files));
	}

	public void addChunk(Team team, Challenge challenge, Long attemptId, Long chunkId, String fileName, String fileType, MultipartFile file) throws AlreadyApprovedException, IOException {
		if (this.submissionRepository.existsByTeamAndChallengeAndStatusIn(team, challenge, List.of(SubmissionState.PENDING, SubmissionState.APPROVED))) throw new AlreadyApprovedException();


		SubmissionAttempt attempt = this.attemptRepository.getReferenceById(new SubmissionAttemptId(team, challenge, attemptId));
		FileSubmission fileLocation = this.fileSubmissionRepository.save(new FileSubmission(fileType, file));
		this.chunkRepository.save(new SubmissionChunk(
				attempt,
				fileName,
				chunkId,
				fileLocation

		));
	}

	@Async @Transactional
	public void processSubmission(Team team, Challenge challenge, Long attemptId) throws IOException, AlreadyApprovedException {
		SubmissionAttempt attempt = this.attemptRepository.findByIdForUpdate(team, challenge, attemptId)
				.orElseThrow(() -> new EntityNotFoundException("Attempt not found"));

		// Check if submission has already been created to avoid redundant processing
		if (this.submissionRepository.existsById(new ChallengeSubmissionId(team, challenge))) {
			return; // Another thread has already processed this submission
		}


		if (this.isAttemptFulfilled(team, challenge, attemptId)) {
			this.turnAttemptIntoSubmission(team, challenge, attemptId);
		}
	}

	public boolean isAttemptFulfilled(Team team, Challenge challenge, Long id) {

		SubmissionAttempt attempt = this.attemptRepository.getReferenceById(new SubmissionAttemptId(team, challenge, id));

		Map<String, Integer> files = attempt.getFiles();
		return files.entrySet().stream().allMatch((entry) -> this.chunkRepository.countByAttemptAndFileName(attempt, entry.getKey()) == entry.getValue());
	}

	@Transactional
	public void turnAttemptIntoSubmission(Team team, Challenge challenge, Long id) throws IOException, AlreadyApprovedException {
		SubmissionAttempt attempt = this.attemptRepository.getReferenceById(new SubmissionAttemptId(team, challenge, id));

		// Use a PESSIMISTIC_WRITE lock to prevent other transactions from modifying this attempt while processing
		attempt = this.attemptRepository.findByIdForUpdate(team, challenge, id).get();


		Map<String, Integer> files = attempt.getFiles();

		SubmissionAttempt finalAttempt = attempt;
		List<FileSubmission> fileSubmissions = files.entrySet().stream().map((entry -> {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			List<SubmissionChunk> chunks = this.chunkRepository.findByAttemptAndFileNameOrderByIdAsc(finalAttempt, entry.getKey());
			for (SubmissionChunk chunk : chunks) {
				try {
					outputStream.write(chunk.getChunk().getContent());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return new FileSubmission(chunks.stream().findFirst().get().getChunk().getType(), outputStream.toByteArray());
		})).toList();

		this.createSubmission(team, challenge, fileSubmissions);
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
										submission.getDeniedReason(),
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

	public void deny(Team team, Long id, String reason) {
		ChallengeSubmission submission = this.submissionRepository.getReferenceById(new ChallengeSubmissionId(team, this.getChallenge(id)));
		submission.deny(reason);
		this.submissionRepository.save(submission);
	}

	public FileSubmission getSubmissionFile(UUID id) {
		return this.fileSubmissionRepository.getReferenceById(id);
	}
}
