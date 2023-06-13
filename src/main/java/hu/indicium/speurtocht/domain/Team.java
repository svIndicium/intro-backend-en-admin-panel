package hu.indicium.speurtocht.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Team {
	@Id
	@GeneratedValue
	private UUID id;

	private String name;

	public Team(String name) {
		this.name = name;
	}
}
