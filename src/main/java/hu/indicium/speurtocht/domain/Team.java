package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
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

	@Column(unique = true)
	private String name;

	public Team(String name) {
		this.name = name;
	}
}
