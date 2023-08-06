package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ChallengeSubmissionRepository extends JpaRepository<ChallengeSubmission, ChallengeSubmissionId> {
	boolean existsByTeamAndStatusIn(Team team, Collection<SubmissionState> statuses);

	boolean existsByTeamAndChallengeAndStatusIn(Team team, Challenge challenge, Collection<SubmissionState> statuses);

//	boolean existsByTeamAndChallengeAndStatus(Team team, Challenge challenge, SubmissionState status);

	List<ChallengeSubmission> findByTeam(Team team);
	List<ChallengeSubmission> findByTeamAndStatus(Team team, SubmissionState status);
	List<ChallengeSubmission> findByStatus(SubmissionState status);

//	@Query("select c from ChallengeSubmission c where c.team = ?1")
//	List<ChallengeSubmissionInfo> findByTeam(Team team);



}
