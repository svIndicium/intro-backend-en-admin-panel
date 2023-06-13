package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.repository.PictureRepository;
import hu.indicium.speurtocht.repository.PictureSubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class PictureService {

	private PictureRepository repository;
	private PictureSubmissionRepository submissionRepository;

	public Picture save(Coordinate coordinate) {
		return this.repository.save(new Picture(coordinate));
	}

	public Picture getPicture(Long id) {
		return this.repository.getReferenceById(id);
	}

	public PictureSubmission createSubmission(Team team, Picture picture, MultipartFile file) throws IOException {
		return this.submissionRepository.save(new PictureSubmission(team, picture, file));
	}

	@Transactional
	public HashMap<Long, List<SubmissionState>> getTeamsPictures(Team team) {
		List<Picture> pictures = this.repository.findAll();
		HashMap<Long, List<SubmissionState>> output = new HashMap<>(pictures.size());
		List<PictureSubmission> submissions = this.submissionRepository.findByTeam(team);
		for (Picture picture : pictures) {
			output.put(
					picture.getId(),
					submissions
							.stream()
							.filter(
									(submission) -> submission.getPicture().getId() == picture.getId())
							.map(PictureSubmission::getStatus)
							.toList()
			);
		}

		return output;




	}
}
