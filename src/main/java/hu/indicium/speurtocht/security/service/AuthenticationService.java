package hu.indicium.speurtocht.security.service;

import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.controller.AuthenticationResponse;

public interface AuthenticationService {
	AuthenticationResponse createUser(Team team, String password);
	AuthenticationResponse createAdmin(String username, String password);

	AuthenticationResponse signin(String username, String password);
}
