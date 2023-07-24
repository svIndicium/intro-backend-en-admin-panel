package hu.indicium.speurtocht.controller.dto;

import java.util.UUID;

public record LeaderboardDTO(UUID id, String teamname, PointsDTO points) {
}
