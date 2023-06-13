package hu.indicium.speurtocht.controller.dto;

import hu.indicium.speurtocht.domain.Team;

import java.util.UUID;

public record SubmissionDTO(UUID id, String teamName, String type) {
}
