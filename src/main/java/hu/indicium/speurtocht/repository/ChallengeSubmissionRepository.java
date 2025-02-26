package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.controller.dto.ChallengeSubmissionDTO;
import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	@Query(value = "SELECT new hu.indicium.speurtocht.controller.dto.ChallengeSubmissionDTO(" +
			"ch.title, ch.challenge, ch.points, t.name, " +
			"ARRAY_AGG(f.id)) " +  // Using ARRAY_AGG to collect UUIDs
			"FROM ChallengeSubmission cs " +
			"JOIN cs.challenge ch " +
			"JOIN cs.team t " +
			"JOIN cs.fileSubmission f " +
			"WHERE ch.id = :challengeId AND t.id = :teamId " +
			"GROUP BY ch.title, ch.challenge, ch.points, t.name", nativeQuery = true)
	ChallengeSubmissionDTO getSubmissionWithoutLob(@Param("challengeId") Long challengeId, @Param("teamId") UUID teamId);


	@Query(value = "SELECT new hu.indicium.speurtocht.dto.ChallengeSubmissionDTO(" +
			"ch.title, ch.challenge, ch.points, t.name, " +
			"ARRAY_AGG(f.id)) " +  // Using ARRAY_AGG to collect UUIDs
			"FROM ChallengeSubmission cs " +
			"JOIN cs.challenge ch " +
			"JOIN cs.team t " +
			"JOIN cs.fileSubmission f " +
			"WHERE ch.id = :challengeId AND t.id = :teamId " +
			"GROUP BY ch.title, ch.challenge, ch.points, t.name",
			nativeQuery = true)
	ChallengeSubmissionDTO getSubmissionWithoutLoba(@Param("challengeId") Long challengeId, @Param("teamId") UUID teamId);

	@Query(value = "SELECT ch.title AS title, " +
			"ch.challenge AS challenge, " +
			"ch.points AS points, " +
			"t.name AS submittedBy, " +
			"ARRAY_AGG(f.id) AS files " +
			"FROM challenge_submission cs " +
			"JOIN challenge ch ON ch.id = cs.challenge_id " +
			"JOIN team t ON t.id = cs.team_id " +
			"JOIN challenge_submission_file_submission f ON f.challenge_submission_challenge_id = cs.challenge_id " +
			"AND f.challenge_submission_team_id = cs.team_id " +
			"WHERE cs.challenge_id = :challengeId AND cs.team_id = :teamId " +
			"GROUP BY ch.title, ch.challenge, ch.points, t.name",
			nativeQuery = true)
	ChallengeSubmissionDTO getSubmissionWithoutLobc(@Param("challengeId") Long challengeId, @Param("teamId") UUID teamId);



	@Query(value = "SELECT ch.title AS title, " +
			"ch.challenge AS challenge, " +
			"ch.points AS points, " +
			"t.name AS submittedBy, " +
			"ARRAY_AGG(fs.id) AS files " +
			"FROM challenge_submission cs " +
			"JOIN challenge ch ON ch.id = cs.challenge_id " +
			"JOIN team t ON t.id = cs.team_id " +
			"JOIN challenge_submission_file_submission csfs ON csfs.challenge_submission_challenge_id = cs.challenge_id " +
			"AND csfs.challenge_submission_team_id = cs.team_id " +
			"JOIN file_submission fs ON fs.id = csfs.file_submission_id " +
			"WHERE cs.challenge_id = :challengeId AND cs.team_id = :teamId " +
			"GROUP BY ch.title, ch.challenge, ch.points, t.name",
			nativeQuery = true)
	List<Object[]> getSubmissionWithoutLobd(@Param("challengeId") Long challengeId, @Param("teamId") UUID teamId);


	@Query(value = "SELECT ch.title AS title, " +
			"ch.challenge AS challenge, " +
			"ch.points AS points, " +
			"t.name AS submittedBy, " +
			"ARRAY_AGG(fs.id) AS files " +
			"FROM challenge_submission cs " +
			"JOIN challenge ch ON ch.id = cs.challenge_id " +
			"JOIN team t ON t.id = cs.team_id " +
			"JOIN challenge_submission_file_submission csfs ON csfs.challenge_submission_challenge_id = cs.challenge_id " +
			"AND csfs.challenge_submission_team_id = cs.team_id " +
			"JOIN file_submission fs ON fs.id = csfs.file_submission_id " +
			"WHERE cs.challenge_id = :challengeId AND cs.team_id = :teamId " +
			"GROUP BY ch.title, ch.challenge, ch.points, t.name",
			nativeQuery = true)
	List<Object[]> getSubmissionRaw(@Param("challengeId") Long challengeId, @Param("teamId") UUID teamId);

//	@Query("select c from ChallengeSubmission c where c.team = ?1")
//	List<ChallengeSubmissionInfo> findByTeam(Team team);



}
