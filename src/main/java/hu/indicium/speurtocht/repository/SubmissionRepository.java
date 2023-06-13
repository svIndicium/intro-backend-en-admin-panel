package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Submission;
import hu.indicium.speurtocht.domain.SubmissionState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
	List<Submission> findByStatus(SubmissionState status);
}
