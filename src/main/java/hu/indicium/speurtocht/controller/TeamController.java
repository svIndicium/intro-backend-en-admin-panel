package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.*;
import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.security.service.impl.AuthenticationServiceImpl;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.utils.ScoreCalculator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Tag(name = "Team")
@RestController
@SecurityRequirement(name = "speurtocht-88")
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

	@Autowired
	private AuthUtils authUtils;
	private TeamService service;
	private PictureService pictureService;
	private ChallengeService challengeService;
	private AuthenticationServiceImpl authenticationService;

	@Secured("ADMIN")
	@Operation(
			summary = "Get current leaderboard",
			description = "Get a list of teams and their score. List is already sorted by order."
	)
	@GetMapping("/leaderboard")
	public List<LeaderboardDTO> leaderboard() {
		return this.service
				.getAll()
				.stream()
				.map(team -> {
					long challengePoints = this.challengeService.getTeamPoints(team);
					long picturesApproved = this.pictureService.getTeamPoints(team);
					PointsDTO score = new PointsDTO(challengePoints, picturesApproved, ScoreCalculator.calculatedScore(challengePoints, picturesApproved));

					return new LeaderboardDTO(team.getId(), team.getName(), score);
				})
				.sorted(new LeaderboardComparator())
				.toList();
	}

	@Secured("PARTICIPANT")
	@Operation(
			summary = "Get my team's score",
			description = "Get amount of challenge points awarded and picture locations are approved."
	)
	@GetMapping("/score")
	public ScoreDTO points() {
		Team team = this.authUtils.getTeam();
		long challengePoints = this.challengeService.getTeamPoints(team);
		long picturesApproved = this.pictureService.getTeamPoints(team);
		return new ScoreDTO(team.getName(), new PointsDTO(challengePoints, picturesApproved, ScoreCalculator.calculatedScore(challengePoints, picturesApproved)));
	}

	private static class LeaderboardComparator implements Comparator<LeaderboardDTO> {

		@Override
		public int compare(LeaderboardDTO o1, LeaderboardDTO o2) {
			PointsDTO p1 = o1.score();
			PointsDTO p2 = o2.score();

			return (int) (p2.points() - p1.points());

		}
	}

	@Secured("ADMIN")
	@Operation(
			summary = "Create a new team",
			description = "Creates a new team, returns their unique join code"
	)
	@PostMapping
	public String createNewTeam(@RequestBody CreateTeamDTO teamDTO) {
		Team team = this.service.save(teamDTO.teamname());
		return authenticationService.createUser(team);
	}

	@Secured("ADMIN")
	@Operation(
			summary = "Get my team's score",
			description = "Get amount of challenge points awarded and picture locations are approved."
	)
	@GetMapping("/{id}")
	public TeamDTO getTeam(@PathVariable UUID id) {
		Team team = this.service.getTeam(id);
		long challengePoints = this.challengeService.getTeamPoints(team);
		long picturesApproved = this.pictureService.getTeamPoints(team);
		ScoreDTO meta = new ScoreDTO(team.getName(), new PointsDTO(challengePoints, picturesApproved, ScoreCalculator.calculatedScore(challengePoints, picturesApproved)));

		String joinCode = team.getJoinCode();
		Collection<PictureSubmissionDTO> pictures = this.pictureService.getTeamsPictures(team).values();
		Collection<ChallengeStatusDTO> challenges = this.challengeService.getTeamChallenges(team).values();

		return new TeamDTO(meta, joinCode, pictures, challenges);
	}
}
