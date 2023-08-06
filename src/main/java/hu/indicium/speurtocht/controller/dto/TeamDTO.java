package hu.indicium.speurtocht.controller.dto;

import java.util.Collection;

public record TeamDTO(ScoreDTO meta, Collection<PictureSubmissionDTO> pictures, Collection<ChallengeStatusDTO> challenges) {
}
