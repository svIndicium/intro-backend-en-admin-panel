package hu.indicium.speurtocht.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Challenge {

	@Id
	@GeneratedValue
	private Long id;

	private String challenge;

	private int points;

	public Challenge(String challenge, int points) {
		this.challenge = challenge;
		this.points = points;
	}
}
