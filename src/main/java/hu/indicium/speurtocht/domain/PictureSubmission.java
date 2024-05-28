package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private FileSubmission fileSubmission;

	public PictureSubmission(Team team, Picture picture, MultipartFile file) throws IOException {
		this.team = team;
		this.picture = picture;
		this.status = SubmissionState.PENDING;
		this.fileSubmission = new FileSubmission(file);
	}

	public void approve() {
		this.status = SubmissionState.APPROVED;
	}

	public void deny(String deniedReason) {
		this.deniedReason = deniedReason;
		this.status = SubmissionState.DENIED;
	}
}
