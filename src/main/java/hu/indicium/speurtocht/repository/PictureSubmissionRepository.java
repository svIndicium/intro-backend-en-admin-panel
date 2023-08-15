package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PictureSubmissionRepository extends JpaRepository<PictureSubmission, PictureSubmissionId> {
	List<PictureSubmission> findByTeam(Team team);
	boolean existsByTeamAndPictureAndStatusIn(Team team, Picture picture, Collection<SubmissionState> statuses);
	long countByTeamAndStatus(Team team, SubmissionState status);
	List<PictureSubmission> findByStatus(SubmissionState status);
}
