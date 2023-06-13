package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import hu.indicium.speurtocht.domain.Team;

import java.util.List;


@RestController
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

	private TeamService service;

	@GetMapping
	public List<Team> leaderboard() {
		return this.service.getAll();
	}

	@PostMapping("/{name}")
	public Team createNewTeam(@PathVariable String name) {
		return service.save(name);
	}
}
