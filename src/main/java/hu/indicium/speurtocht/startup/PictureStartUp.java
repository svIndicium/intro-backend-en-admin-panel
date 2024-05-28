package hu.indicium.speurtocht.startup;

import hu.indicium.speurtocht.domain.Coordinate;
import hu.indicium.speurtocht.domain.Picture;
import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.utils.StartUpMultipartFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Component
@Slf4j
@AllArgsConstructor
@Order(2)
@Transactional
public class PictureStartUp implements ApplicationRunner {

	private TeamService teamService;
	private PictureService pictureService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!args.containsOption("fill")) return;

		Path path = Paths.get("./nature-1.jpg");
		String name = "nature-1.jpg";
		String originalFileName = "nature-1.jpg";
		String contentType = "image/jpg";
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
			System.out.println(e);
		}
		MultipartFile result = new StartUpMultipartFile(name,
				originalFileName, contentType, content);

		Team team = teamService.getTeamByName("team-1");
		for (int i = 0; i < 25; i++) {
			Picture picture = this.pictureService.createPictures(new Coordinate(1.0f, 1.0f), result);
			log.info("created location: " + i);
			int randomInt = new Random().nextInt(25);
			if (randomInt >= 16) {
				this.pictureService.createSubmission(team, picture, result);
				log.info("created picture submission for: " + i);
				if (randomInt >= 19) {
					if (randomInt >= 22) {
						this.pictureService.approve(team, picture.getId());
						log.info("approved picture submission for: " + i);
					} else {
						this.pictureService.deny(team, picture.getId(), "Lorem ipsum dolor sit amet");
						log.info("denied picture submission for: " + i);
					}
				}
			}
		}
	}
}
