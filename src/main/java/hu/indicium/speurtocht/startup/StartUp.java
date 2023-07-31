package hu.indicium.speurtocht.startup;

import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.service.impl.AuthenticationServiceImpl;
import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@Order(1)
public class StartUp implements CommandLineRunner {

	private TeamService teamService;

	private AuthenticationServiceImpl authenticationService;

	@Override
	public void run(String... args) throws Exception {
		if (this.teamService.getAll().size() == 0) {
			for (int i = 1; i < 11; i++) {
				Team team = this.teamService.save("team-" + i);
				authenticationService.createUser(team, "password");
			}
		}

		authenticationService.createAdmin("admin", "admin");
	}
}
