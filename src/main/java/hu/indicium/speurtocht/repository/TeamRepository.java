package hu.indicium.speurtocht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hu.indicium.speurtocht.domain.Team;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
}
