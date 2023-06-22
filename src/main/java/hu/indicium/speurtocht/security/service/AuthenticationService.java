package hu.indicium.speurtocht.security.service;

import hu.indicium.speurtocht.security.controller.AuthenticationResponse;

public interface AuthenticationService {
	AuthenticationResponse createUser(String teamname, String password);
	AuthenticationResponse createAdmin(String username, String password);

	AuthenticationResponse signin(String username, String password);
}
