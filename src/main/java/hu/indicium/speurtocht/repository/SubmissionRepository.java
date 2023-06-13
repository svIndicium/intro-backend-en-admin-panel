package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
