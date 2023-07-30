package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.CreateTeamDTO;
import hu.indicium.speurtocht.controller.dto.LeaderboardDTO;
import hu.indicium.speurtocht.controller.dto.PointsDTO;
import hu.indicium.speurtocht.controller.dto.ScoreDTO;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.security.service.impl.AuthenticationServiceImpl;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import hu.indicium.speurtocht.domain.Team;

import java.util.Comparator;
import java.util.List;


@Tag(name = "Team")
@RestController
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

	@Autowired
	private AuthUtils authUtils;
	private TeamService service;
	private PictureService pictureService;
	private ChallengeService challengeService;
	private AuthenticationServiceImpl authenticationService;

	@Operation(
			summary = "Get current leaderboard",
			description = "Get a list of teams and their score. List is already sorted by order."
	)
	@GetMapping("/leaderboard")
	public List<LeaderboardDTO> leaderboard() {
		return this.service
				.getAll()
				.stream()
				.map(team -> new LeaderboardDTO(team.getId(), team.getName(), new PointsDTO(this.challengeService.getTeamPoints(team), this.pictureService.getTeamPoints(team))))
				.sorted(new LeaderboardComparator())
				.toList();
	}

	@Operation(
			summary = "Get my team's score",
			description = "Get amount of challenge points awarded and picture locations are approved."
	)
	@GetMapping("/score")
	public ScoreDTO points() {
		Team team = this.authUtils.getTeam();
		return new ScoreDTO(team.getName(), new PointsDTO(this.challengeService.getTeamPoints(team), this.pictureService.getTeamPoints(team)));
	}

	private static class LeaderboardComparator implements Comparator<LeaderboardDTO> {

		@Override
		public int compare(LeaderboardDTO o1, LeaderboardDTO o2) {
			PointsDTO p1 = o1.points();
			PointsDTO p2 = o2.points();
			if (p1.challengePoints() == p2.challengePoints()) {
				return (int) (p2.picturesApproved() - p1.picturesApproved());
			} else {
				return (int) (p2.challengePoints() - p1.challengePoints());
			}
		}
	}

	@Operation(
			summary = "Create a new team"
	)
	@PostMapping
	public void createNewTeam(@RequestBody CreateTeamDTO teamDTO) {
		Team team = this.service.save(teamDTO.teamname());
		authenticationService.createUser(team, teamDTO.password());
	}
}
