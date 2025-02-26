package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.SubmissionAttempt;
import hu.indicium.speurtocht.domain.SubmissionChunk;
import hu.indicium.speurtocht.domain.SubmissionChunkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionChunkRepository extends JpaRepository<SubmissionChunk, SubmissionChunkId> {

    long countByAttemptAndFileName(SubmissionAttempt attempt, String fileName);

    List<SubmissionChunk> findByAttemptAndFileNameOrderByIdAsc(SubmissionAttempt attempt, String fileName);



}
