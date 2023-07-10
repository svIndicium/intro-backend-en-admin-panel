package hu.indicium.speurtocht;

import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.security.domain.User;
import hu.indicium.speurtocht.security.repository.UserRepository;
import hu.indicium.speurtocht.security.service.impl.AuthenticationServiceImpl;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartUp implements CommandLineRunner {

	private TeamService teamService;
	private PictureService pictureService;
	private ChallengeService challengeService;

	private AuthenticationServiceImpl authenticationService;

	@Override
	public void run(String... args) throws Exception {
		if (this.teamService.getAll().size() == 0) {
			Team team = this.teamService.save("team-1");
			authenticationService.createUser(team, "password");
		}


		authenticationService.createAdmin("admin", "admin");

//		User user = this.userRepository.save(User.createParticipant(team, new BCryptPasswordEncoder().encode("password")));
//		System.out.println(team);
//		System.out.println(user);

		for (int i = 0; i < 25; i++) {
			Picture picture = this.pictureService.save(new Coordinate(1.0f, 1.0f));
//			PictureSubmission submission = this.pictureService.createSubmission(team, picture, null);

//			System.out.println(submission);
//			System.out.println(picture);

		}

		for (int i = 0; i < 88; i++) {
			Challenge challenge = this.challengeService.save("hello world", 30);
//			System.out.println(challenge);
		}
	}
}
