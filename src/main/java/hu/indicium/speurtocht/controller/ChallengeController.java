package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.controller.dto.ChallengeStatusDTO;
import hu.indicium.speurtocht.controller.dto.ChallengeSubmissionDTO;
import hu.indicium.speurtocht.controller.dto.CreateChallengeDTO;
import hu.indicium.speurtocht.controller.dto.SubmissionDTO;
import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Tag(
		name = "Crazy 88",
		description = "Define 88 challenges that teams can complete. Teams compete against each other. The winner is the team that scores the most points."
)
@RestController
@SecurityRequirement(name = "speurtocht-88")
@RequestMapping("/challenges")
@AllArgsConstructor
public class ChallengeController {

	@Autowired
	private AuthUtils authUtils;

	private ChallengeService challengeService;
	private TeamService teamService;

	@Secured("ADMIN")
	@Operation(summary = "Create challenges")
	@PostMapping
	public void createChallenges(@RequestBody List<CreateChallengeDTO> challenges) {
		// todo maximum of 88 challenges
		this.challengeService.createBulk(challenges.stream().map(challenge -> new Challenge(challenge.title(), challenge.challenge(), challenge.points())).toList());
	}

	@Operation(summary = "Get list of challenges")
	@GetMapping
	public Collection<ChallengeStatusDTO> getChallenges() {
		return this.challengeService.getTeamChallenges(this.authUtils.getTeam()).values();
	}

	@Secured("PARTICIPANT")
	@Operation(summary = "Create submission for a challenge")
	@ApiResponses({
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "Failed to parse image."),
			@ApiResponse(responseCode = "403", description = "There's already a image submitted for this challenge.")
	})
	@PostMapping(value = "/{challengeId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void createSubmission(@PathVariable Long challengeId, @RequestParam("files") MultipartFile[] files) {
		try {
			this.challengeService.createSubmission(
					this.authUtils.getTeam(),
					this.challengeService.getChallenge(challengeId),
					files
			);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	@Secured("ADMIN")
	@Operation(summary = "Get list of Crazy 88 submissions awaiting approval")
	@GetMapping("/pending")
	public List<SubmissionDTO> getPending() {
		return this.challengeService
				.getPending()
				.stream()
				.map((submission -> new SubmissionDTO(submission.getChallenge().getId(), submission.getTeam().getName(), submission.getTeam().getId())))
				.toList();
	}

	@Secured("ADMIN")
	@Operation(summary = "Get data about a Crazy 88 submission")
	@GetMapping("/{challengeId}/teams/{teamId}")
	@Transactional
	public ChallengeSubmissionDTO getSubmission(@PathVariable Long challengeId, @PathVariable UUID teamId) {
		ChallengeSubmission submission = this.challengeService.getSubmission(this.teamService.getTeam(teamId), challengeId);
		return new ChallengeSubmissionDTO(
				submission.getChallenge().getTitle(),
				submission.getChallenge().getChallenge(),
				submission.getChallenge().getPoints(),
				submission.getTeam().getName(),
				submission.getFileSubmission().stream().map(FileSubmission::getId).toList()
		);
	}

	@Secured("ADMIN")
	@Operation(summary = "Approve a Crazy 88 submission")
	@PatchMapping("/{challengeId}/teams/{teamId}/approve")
	public void approve(@PathVariable Long challengeId, @PathVariable UUID teamId) {
		this.challengeService.approve(this.teamService.getTeam(teamId), challengeId);
	}

	@Secured("ADMIN")
	@Operation(summary = "Deny a Crazy 88 submission")
	@PatchMapping("/{challengeId}/teams/{teamId}/deny")
	public void deny(@PathVariable Long challengeId, @PathVariable UUID teamId) {
		this.challengeService.deny(this.teamService.getTeam(teamId), challengeId);
	}

	@Secured("ADMIN")
	@Operation(summary = "Get a file from a Crazy 88 submission")
	@GetMapping("/submissions/{fileId}/file")
	@Transactional
	public ResponseEntity<byte[]> getContent(@PathVariable UUID fileId) {
		HttpHeaders responseHeaders = new HttpHeaders();
		FileSubmission file = this.challengeService.getSubmissionFile(fileId);
		responseHeaders.set("Content-Type", file.getType());
		return ResponseEntity
				.ok()
				.headers(responseHeaders)
				.body(file.getContent());
	}
}
