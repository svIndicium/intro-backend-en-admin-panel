package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Picture;
import hu.indicium.speurtocht.domain.PictureSubmission;
import hu.indicium.speurtocht.domain.SubmissionState;
import hu.indicium.speurtocht.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PictureSubmissionRepository extends JpaRepository<PictureSubmission, Long> {
	List<PictureSubmission> findByTeam(Team team);
	boolean existsByTeamAndPictureAndStatus(Team team, Picture picture, SubmissionState status);
	long countByTeamAndStatus(Team team, SubmissionState status);
}
