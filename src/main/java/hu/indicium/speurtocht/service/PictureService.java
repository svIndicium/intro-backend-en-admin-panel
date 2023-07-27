package hu.indicium.speurtocht.service;

import hu.indicium.speurtocht.controller.dto.ChallengeStatusDTO;
import hu.indicium.speurtocht.controller.dto.PictureSubmissionDTO;
import hu.indicium.speurtocht.domain.*;
import hu.indicium.speurtocht.repository.FileSubmissionRepository;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PictureService {

	private PictureRepository repository;
	private PictureSubmissionRepository submissionRepository;
	private FileSubmissionRepository fileSubmissionRepository;


	public Picture createPictures(Coordinate coordinate, MultipartFile file) throws IOException {
		return this.repository.save(new Picture(coordinate, file));
	}

	public Picture getPicture(Long id) {
		return this.repository.getReferenceById(id);
	}

	public PictureSubmission getSubmission(Team team, Long id) {
		return this.submissionRepository.getReferenceById(new PictureSubmissionId(team, this.repository.getReferenceById(id)));
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
	public Map<Long, PictureSubmissionDTO> getTeamsPictures(Team team) {
		Map<Long, PictureSubmissionDTO> collected = this.repository
				.findAll()
				.stream()
				.collect(
						Collectors.toMap(
								Picture::getId,
								(picture) -> new PictureSubmissionDTO(
										picture.getId(),
										null
								)
						)
				);

		Map<Long, PictureSubmissionDTO> submitted = this.submissionRepository
				.findByTeam(team)
				.stream()
				.collect(
						Collectors.toMap(
								(e) -> e.getPicture().getId(),
								(submission) -> new PictureSubmissionDTO(
										submission.getPicture().getId(),
										submission.getStatus()
								)
						)
				);

		collected.putAll(submitted);

		return collected;
	}

	public PictureFile getFile(Long id) {
		return this.repository.getReferenceById(id).getFile();
	}

	public FileSubmission getSubmissionFile(Team team, Long id) {
		return this.submissionRepository.getReferenceById(new PictureSubmissionId(team, this.getPicture(id))).getFileSubmission();
	}

	public List<PictureSubmission> getPending() {
		return this.submissionRepository.findByStatus(SubmissionState.PENDING);
	}

	public void approve(Team team, Long id) {
		PictureSubmission submission = this.submissionRepository.getReferenceById(new PictureSubmissionId(team, this.getPicture(id)));
		submission.approve();
		this.submissionRepository.save(submission);
	}

	public void deny(Team team, Long id) {
		PictureSubmission submission = this.submissionRepository.getReferenceById(new PictureSubmissionId(team, this.getPicture(id)));
		submission.deny();
		this.submissionRepository.save(submission);
	}
}
