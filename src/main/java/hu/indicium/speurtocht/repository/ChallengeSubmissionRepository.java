package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeSubmissionRepository extends JpaRepository<ChallengeSubmission, Long> {
	boolean existsByTeamAndChallengeAndStatus(Team team, Challenge challenge, SubmissionState status);
	List<ChallengeSubmission> findByTeam(Team team);
	List<ChallengeSubmission> findByTeamAndStatus(Team team, SubmissionState status);
}
