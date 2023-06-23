package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.repository.ChallengeRepository;
import hu.indicium.speurtocht.repository.ChallengeSubmissionRepository;
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

	public Challenge save(String challenge, int points) {
		return this.repository.save(new Challenge(challenge, points));
	}

	public Challenge getChallenge(Long id) {
		return this.repository.getReferenceById(id);
	}
	public ChallengeSubmission createSubmission(Team team, Challenge challenge, MultipartFile file) throws IOException {
		return this.submissionRepository.save(new ChallengeSubmission(team, challenge, file));
	}

	@Transactional
	public HashMap<Long, List<SubmissionState>> getTeamsPictures(Team team) {
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
}
