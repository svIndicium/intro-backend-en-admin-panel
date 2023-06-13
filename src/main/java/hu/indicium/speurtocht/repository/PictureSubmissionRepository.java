package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.PictureSubmission;
import hu.indicium.speurtocht.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureSubmissionRepository extends JpaRepository<PictureSubmission, Long> {
	List<PictureSubmission> findByTeam(Team team);

}
