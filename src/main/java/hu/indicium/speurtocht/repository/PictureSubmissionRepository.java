package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PictureSubmissionRepository extends JpaRepository<PictureSubmission, PictureSubmissionId> {
	List<PictureSubmission> findByTeam(Team team);
	boolean existsByTeamAndPictureAndStatusIn(Team team, Picture picture, Collection<SubmissionState> statuses);
	long countByTeamAndStatus(Team team, SubmissionState status);
	List<PictureSubmission> findByStatus(SubmissionState status);
}
