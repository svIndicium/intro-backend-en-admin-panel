package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.repository.PictureRepository;
import hu.indicium.speurtocht.repository.PictureSubmissionRepository;
import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PictureService {

	private PictureRepository repository;
	private PictureSubmissionRepository submissionRepository;

	public Picture createPictures(Coordinate coordinate, MultipartFile file) throws IOException {
		return this.repository.save(new Picture(coordinate, file));
	}

	public Picture getPicture(Long id) {
		return this.repository.getReferenceById(id);
	}

	public PictureSubmission getSubmission(UUID id) {
		return this.submissionRepository.getReferenceById(id);
	}

	public List<Picture> getAll() {
		return this.repository.findAll();
	}

	public PictureSubmission createSubmission(Team team, Picture picture, MultipartFile file) throws IOException, AlreadyApprovedException {
		if (this.submissionRepository.existsByTeamAndPictureAndStatus(team, picture, SubmissionState.APPROVED)) throw new AlreadyApprovedException();
		return this.submissionRepository.save(new PictureSubmission(team, picture, file));
	}

	public long getTeamPoints(Team team) {
		return this.submissionRepository.countByTeamAndStatus(team, SubmissionState.APPROVED);
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

	public PictureFile getFile(Long id) {
		return this.repository.getReferenceById(id).getFile();
	}

	public FileSubmission getSubmissionFile(UUID id) {
		return this.submissionRepository.getReferenceById(id).getFileSubmission();
	}

	public List<Submission> getPending() {
		return this.submissionRepository.findByStatus(SubmissionState.PENDING);
	}

	public void approve(UUID id) {
		PictureSubmission submission = this.submissionRepository.getReferenceById(id);
		submission.approve();
		this.submissionRepository.save(submission);
	}

	public void deny(UUID id) {
		PictureSubmission submission = this.submissionRepository.getReferenceById(id);
		submission.deny();
		this.submissionRepository.save(submission);
	}
}
