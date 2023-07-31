package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(ChallengeSubmissionId.class)
public class ChallengeSubmission {

	@ManyToOne
	@Id
	private Team team;

	@ManyToOne
	@Id
	private Challenge challenge;

	private SubmissionState status;

	private Instant submittedAt;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FileSubmission> fileSubmission;

	public ChallengeSubmission(Team team, Challenge challenge, MultipartFile[] files) throws IOException {
		this.team = team;
		this.challenge = challenge;
		this.status = SubmissionState.PENDING;
		this.submittedAt = Instant.now();
		this.fileSubmission = Arrays.stream(files).map(file -> {
			try {
				return new FileSubmission(file);
			} catch (IOException e) {
				System.out.println(e);
				throw new RuntimeException(e);
			}
		}).toList();
	}

	public void approve() {
		this.status = SubmissionState.APPROVED;
	}

	// todo string met reden waarom
	public void deny() {
		this.status = SubmissionState.DENIED;
	}
}
