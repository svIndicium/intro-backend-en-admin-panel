package hu.indicium.speurtocht.controller.dto;

import hu.indicium.speurtocht.domain.SubmissionState;

public record ChallengeStatusDTO(
		Long id,
		String title,
		String challenge,
		int points,
		SubmissionState state) {
}
