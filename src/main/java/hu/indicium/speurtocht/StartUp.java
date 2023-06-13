package hu.indicium.speurtocht;

import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartUp implements CommandLineRunner {

	private TeamService teamService;
	private PictureService pictureService;
	private ChallengeService challengeService;

	@Override
	public void run(String... args) throws Exception {
		Team team = this.teamService.save("team-1");
		System.out.println(team);

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
