package hu.indicium.speurtocht.controller;

import hu.indicium.speurtocht.domain.PictureSubmission;
import hu.indicium.speurtocht.domain.SubmissionState;
import hu.indicium.speurtocht.service.PictureService;
import hu.indicium.speurtocht.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teams/{teamId}/pictures")
@AllArgsConstructor
public class PictureController {

	private PictureService pictureService;
	private TeamService teamService;

	@GetMapping
	public Map<Long, List<SubmissionState>> getTeamPictures(@PathVariable String teamId) {
		return this.pictureService.getTeamsPictures(this.teamService.getTeam(teamId));
	}

	@PostMapping(value = "/{pictureId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] createSubmission(@PathVariable String teamId, @PathVariable Long pictureId, @RequestParam("file") MultipartFile file) {



		try {
			PictureSubmission submission = this.pictureService.createSubmission(
				this.teamService.getTeam(teamId),
				this.pictureService.getPicture(pictureId),
				file
			);

			return submission.getFileSubmission().getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
//		return submission;
	}
}
