package hu.indicium.speurtocht.startup;

import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.service.impl.AuthenticationServiceImpl;
import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@Order(1)
public class StartUp implements ApplicationRunner {

	private TeamService teamService;

	private AuthenticationServiceImpl authenticationService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!args.containsOption("fill")) return;

		if (this.teamService.getAll().isEmpty()) {
			for (int i = 1; i < 11; i++) {
				Team team = this.teamService.save("team-" + i);
				String joinCode = authenticationService.createUser(team);
				log.info("created team:\t{} with join code:\t{}", team.getName(), joinCode);
			}
		}

		authenticationService.createAdmin("admin", "admin");
	}
}
