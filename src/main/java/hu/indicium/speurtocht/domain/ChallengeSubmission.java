package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChallengeSubmission extends Submission {

	@ManyToOne
	@JoinColumn(name = "challenge_id")
	private Challenge challenge;


	public ChallengeSubmission(Team team, Challenge challenge, MultipartFile file) throws IOException {
		super(team, file);
		this.challenge = challenge;
	}


}
