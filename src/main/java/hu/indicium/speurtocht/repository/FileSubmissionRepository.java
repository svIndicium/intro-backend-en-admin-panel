package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.FileSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileSubmissionRepository extends JpaRepository<FileSubmission, UUID> {
}
