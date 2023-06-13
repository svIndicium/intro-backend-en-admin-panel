package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hu.indicium.speurtocht.domain.Team;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeamService {

	private TeamRepository repository;

	public Team save(String name) {
		return repository.save(new Team(name));
	}

	public Team getTeam(UUID id) {
		return this.repository.getReferenceById(id);
	}

	public List<Team> getAll() {
		return repository.findAll();
	}
}
