package hu.indicium.speurtocht.repository;

import hu.indicium.speurtocht.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
	Team findByName(String name);

	Team findByJoinCode(String joinCode);

}
