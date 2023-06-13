package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
