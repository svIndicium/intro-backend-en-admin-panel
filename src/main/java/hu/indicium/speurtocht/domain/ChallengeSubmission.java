package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChallengeSubmission {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	@ManyToOne
	@JoinColumn(name = "challenge_id")
	private Challenge challenge;

	private SubmissionState status;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FileSubmission> fileSubmission;


	public ChallengeSubmission(Team team, Challenge challenge, MultipartFile[] files) throws IOException {
		this.team = team;
		this.challenge = challenge;
		this.status = SubmissionState.PENDING;
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
