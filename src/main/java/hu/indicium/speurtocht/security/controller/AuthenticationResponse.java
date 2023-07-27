package hu.indicium.speurtocht.security.controller;

import hu.indicium.speurtocht.security.domain.UserRole;

public record AuthenticationResponse(String accessToken, UserRole role) {
}
