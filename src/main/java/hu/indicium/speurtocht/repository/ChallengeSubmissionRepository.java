package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChallengeSubmissionRepository extends JpaRepository<ChallengeSubmission, UUID> {
	boolean existsByTeamAndChallengeAndStatus(Team team, Challenge challenge, SubmissionState status);
	List<ChallengeSubmission> findByTeam(Team team);
	List<ChallengeSubmission> findByTeamAndStatus(Team team, SubmissionState status);
	List<ChallengeSubmission> findByStatus(SubmissionState status);
}
