package hu.indicium.speurtocht.security.service.impl;

import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.controller.AuthenticationResponse;
import hu.indicium.speurtocht.security.domain.User;
import hu.indicium.speurtocht.security.repository.UserRepository;
import hu.indicium.speurtocht.security.service.AuthenticationService;
import hu.indicium.speurtocht.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Override
	public AuthenticationResponse createUser(Team team, String password) {
		var user = User.createParticipant(team, passwordEncoder.encode(password));
		userRepository.save(user);
		var jwt = jwtService.generateToken(user);

		return new AuthenticationResponse(jwt, user.getRole());
	}

	@Override
	public AuthenticationResponse createAdmin(String username, String password) {
		var user = User.createAdmin(username, passwordEncoder.encode(password));
		userRepository.save(user);
		var jwt = jwtService.generateToken(user);

		return new AuthenticationResponse(jwt, user.getRole());
	}

	@Override
	public AuthenticationResponse signin(String username, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

		Team team = user.getTeam();
		var jwt = jwtService.generateToken(user);

		return new AuthenticationResponse(jwt, user.getRole());
	}
}
