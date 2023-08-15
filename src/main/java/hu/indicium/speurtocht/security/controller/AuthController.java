package hu.indicium.speurtocht.security.controller;

import hu.indicium.speurtocht.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationService authenticationService;

	@Operation(
			summary = "Login"
	)
	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {
		return this.authenticationService.signin(authenticationRequest.username(), authenticationRequest.password());
	}
}
