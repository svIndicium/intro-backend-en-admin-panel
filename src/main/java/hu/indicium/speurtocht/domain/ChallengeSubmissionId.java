package hu.indicium.speurtocht.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChallengeSubmissionId implements Serializable {

	private Team team;
	private Challenge challenge;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChallengeSubmissionId that)) return false;
		return Objects.equals(team, that.team) && Objects.equals(challenge, that.challenge);
	}

	@Override
	public int hashCode() {
		return Objects.hash(team, challenge);
	}
}
