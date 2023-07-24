package hu.indicium.speurtocht.controller.dto;

import java.util.List;
import java.util.UUID;

public record ChallengeSubmissionDTO(String title, String challenge, int defaultPoints, String submittedBy, List<UUID> files) {
}
