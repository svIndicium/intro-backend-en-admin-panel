package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.domain.Challenge;
import hu.indicium.speurtocht.domain.SubmissionState;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenges")
@AllArgsConstructor
public class ChallengeController {

	@Autowired
	private AuthUtils authUtils;

	private ChallengeService challengeService;

	@PostMapping
	public void createChallenges(@RequestBody List<String> challenges) {
		this.challengeService.createBulk(challenges.stream().map(s -> new Challenge(s, 30)).toList());
	}

	@GetMapping
	public List<String> pictures() {
		return this.challengeService.getAll().stream().map(Challenge::getChallenge).toList();
	}

	@GetMapping("/team")
	public Map<Long, List<SubmissionState>> getTeamChallenges() {
		return this.challengeService.getTeamChallenges(authUtils.getTeam());
	}

	@PostMapping(value = "/{challengeId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void createSubmission(@PathVariable Long challengeId, @RequestParam("file") MultipartFile file) {
		try {
			this.challengeService.createSubmission(
					this.authUtils.getTeam(),
					this.challengeService.getChallenge(challengeId),
					file
			);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}
}
