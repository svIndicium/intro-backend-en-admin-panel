package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Challenge;
import hu.indicium.speurtocht.domain.SubmissionAttempt;
import hu.indicium.speurtocht.domain.SubmissionAttemptId;
import hu.indicium.speurtocht.domain.Team;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmissionAttemptRepository extends JpaRepository<SubmissionAttempt, SubmissionAttemptId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM SubmissionAttempt a WHERE a.team = :team AND a.challenge = :challenge AND a.id = :id")
    Optional<SubmissionAttempt> findByIdForUpdate(@Param("team") Team team, @Param("challenge") Challenge challenge, @Param("id") Long id);

}
