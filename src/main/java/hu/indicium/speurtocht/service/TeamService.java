package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeamService {

	private TeamRepository repository;

	public Team save(String name) {
		SecureRandom secureRandom = new SecureRandom();
		int intPassword = 100000 + secureRandom.nextInt(900000);
		String joinCode = String.valueOf(intPassword);
		return repository.save(new Team(name, joinCode));
	}

	public Team getTeam(UUID id) {
		return this.repository.getReferenceById(id);
	}

	public List<Team> getAll() {
		return repository.findAll();
	}

	public Team getTeamByName(String name) {
		return this.repository.findByName(name);
	}
}
