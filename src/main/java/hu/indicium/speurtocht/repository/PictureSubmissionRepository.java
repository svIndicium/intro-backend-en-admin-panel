package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PictureSubmissionRepository extends JpaRepository<PictureSubmission, UUID> {
	List<PictureSubmission> findByTeam(Team team);
	boolean existsByTeamAndPictureAndStatus(Team team, Picture picture, SubmissionState status);
	long countByTeamAndStatus(Team team, SubmissionState status);
	List<Submission> findByStatus(SubmissionState status);
}
