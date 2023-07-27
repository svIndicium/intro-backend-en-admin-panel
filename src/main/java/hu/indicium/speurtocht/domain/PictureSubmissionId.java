package hu.indicium.speurtocht.domain;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PictureSubmissionId implements Serializable {

	private Team team;
	private Picture picture;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PictureSubmissionId that)) return false;
		return Objects.equals(picture, that.picture) && Objects.equals(team, that.team);
	}

	@Override
	public int hashCode() {
		return Objects.hash(picture, team);
	}
}
