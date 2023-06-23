package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.ChallengeSubmission;
import hu.indicium.speurtocht.domain.PictureSubmission;
import hu.indicium.speurtocht.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeSubmissionRepository extends JpaRepository<ChallengeSubmission, Long> {
	List<ChallengeSubmission> findByTeam(Team team);
}
