package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.domain.PictureSubmission;
import hu.indicium.speurtocht.domain.SubmissionState;
import hu.indicium.speurtocht.security.AuthUtils;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pictures")
@AllArgsConstructor
public class PictureController {

	@Autowired
	private AuthUtils authUtils;

	private PictureService pictureService;

	@GetMapping
	public Map<Long, List<SubmissionState>> getTeamPictures() {
		return this.pictureService.getTeamsPictures(authUtils.getTeam());
	}

	@PostMapping(value = "/{pictureId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void createSubmission(@PathVariable Long pictureId, @RequestParam("file") MultipartFile file) {
		try {
			this.pictureService.createSubmission(
				this.authUtils.getTeam(),
				this.pictureService.getPicture(pictureId),
				file
			);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AlreadyApprovedException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}
}
