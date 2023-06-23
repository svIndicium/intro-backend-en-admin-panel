package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.LeaderboardDTO;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import hu.indicium.speurtocht.domain.Team;

import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

	private TeamService service;
	private PictureService pictureService;
	private ChallengeService challengeService;

	@GetMapping("/leaderboard")
	public List<LeaderboardDTO> leaderboard() {
		return this.service
				.getAll()
				.stream()
				.map(team -> new LeaderboardDTO(team.getName(), this.challengeService.getTeamPoints(team), this.pictureService.getTeamPoints(team)))
				.sorted(new LeaderboardComparator())
				.toList();
	}

	private static class LeaderboardComparator implements Comparator<LeaderboardDTO> {

		@Override
		public int compare(LeaderboardDTO o1, LeaderboardDTO o2) {
			if (o1.ChallengePoints() == o2.ChallengePoints()) {
				return (int) (o2.picturesApproved() - o1.picturesApproved());
			} else {
				return (int) (o2.ChallengePoints() - o1.ChallengePoints());
			}
		}
	}

	@PostMapping("/{name}")
	public Team createNewTeam(@PathVariable String name) {
		return service.save(name);
	}
}
