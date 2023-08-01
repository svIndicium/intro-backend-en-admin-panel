package hu.indicium.speurtocht.startup;

import hu.indicium.speurtocht.domain.Coordinate;
import hu.indicium.speurtocht.domain.Picture;
import hu.indicium.speurtocht.domain.Team;
import hu.indicium.speurtocht.security.service.impl.AuthenticationServiceImpl;
import hu.indicium.speurtocht.service.ChallengeService;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.utils.StartUpMultipartFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@AllArgsConstructor
@Slf4j
public class ZipStartUp implements ApplicationRunner {

	private PictureService pictureService;
	private ChallengeService challengeService;
	private TeamService teamService;
	private AuthenticationServiceImpl authenticationService;

	private final static String unzippedLocation = "unzipped";
	private static final int BUFFER_SIZE = 4096;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		List<String> username = args.getOptionValues("username");
		List<String> password = args.getOptionValues("password");
		List<String> zipLocation = args.getOptionValues("zip");

		if (zipLocation != null) {
			unzip(zipLocation.get(0));
			readPictures();
			readChallenges();
			readTeams();
		}

		if (username != null && password != null) {
			this.authenticationService.createAdmin(username.get(0), password.get(0));
			log.info("Created admin with name:\t" + username.get(0));
		}

		if (zipLocation != null || (username != null && password != null)) {
			log.info("Zip start up completed");
		}
	}

	public void readPictures() throws IOException {
		File dir = new File(unzippedLocation + File.separator + "speurtocht-88" + File.separator + "locations");
		for (File file : Arrays.stream(dir.listFiles()).sorted(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return Integer.parseInt(o1.getName().replaceAll( "[^\\d,]", "" )) - Integer.parseInt(o2.getName().replaceAll( "[^\\d,]", "" ));
			}
		}).toList()) {
			byte[] content = null;
			try {
				content = Files.readAllBytes(file.toPath());
			} catch (final IOException e) {

				log.error("Failed to read file", e);
			}
			MultipartFile result = new StartUpMultipartFile(file.getName(),
					file.getName(), "image/png", content);
			Picture picture = this.pictureService.createPictures(new Coordinate(1.0f, 1.0f), result);
			log.info("Created picture:\t" + file.getName());
		}
	}

	public void readChallenges() throws IOException {
		Files.lines(Paths.get(unzippedLocation + File.separator + "speurtocht-88" + File.separator + "challenges.tsv"))
				.skip(1)
				.forEach(line -> {
					String[] split = line.split("	");
					this.challengeService.save(split[1], split[2], Integer.parseInt(split[3]));
					log.info("Created challenge:\t" + split[0] + "\t" + split[1]);
				});
	}

	public void readTeams() throws IOException {
		Files.lines(Paths.get(unzippedLocation + File.separator + "speurtocht-88" + File.separator + "teams.tsv"))
				.skip(1)
				.forEach(line -> {
					String[] split = line.split("	");
					Team team = this.teamService.save(split[0]);
					this.authenticationService.createUser(team, split[1]);
					log.info("Created team:\t" + split[0]);
				});
	}

	public void unzip(String zipFilePath) throws IOException {
		File destDir = new File(unzippedLocation);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			if (!entry.getName().startsWith("speurtocht-88/")) {
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
				continue;
			}
			String filePath = unzippedLocation + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdirs();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		log.info("Completed writing files to " + unzippedLocation);
	}

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}
}
