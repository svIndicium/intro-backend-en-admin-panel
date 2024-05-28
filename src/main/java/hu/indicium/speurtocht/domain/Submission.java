package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Submission {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	private SubmissionState status;

	private String deniedReason;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private FileSubmission fileSubmission;

	public Submission(Team team, MultipartFile file) throws IOException {
		this.team = team;
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
