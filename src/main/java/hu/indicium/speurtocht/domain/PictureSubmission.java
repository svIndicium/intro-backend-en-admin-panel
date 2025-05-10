package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Entity
//@IdClass(PictureSubmissionId.class)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@IdClass(PictureSubmissionId.class)
public class PictureSubmission {

	@Id
	@ManyToOne
	private Picture picture;

	@Id
	@ManyToOne
	private Team team;

	private SubmissionState status;
	private String deniedReason;
	private UUID fileId;

	public PictureSubmission(Team team, Picture picture, UUID fileId) {
		this.team = team;
		this.picture = picture;
		this.status = SubmissionState.PENDING;
		this.fileId = fileId;
	}

	public void approve() {
		this.status = SubmissionState.APPROVED;
	}

	public void deny(String deniedReason) {
		this.deniedReason = deniedReason;
		this.status = SubmissionState.DENIED;
	}
}
