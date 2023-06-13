package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.Challenge;
import hu.indicium.speurtocht.repository.ChallengeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChallengeService {

	private ChallengeRepository repository;

	public Challenge save(String challenge, int points) {
		return this.repository.save(new Challenge(challenge, points));
	}
}
