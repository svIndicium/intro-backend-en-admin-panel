package hu.indicium.speurtocht.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
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

	@JsonIgnore
	@Column(unique = true)
	private String joinCode;

	public Team(String name, String joinCode) {
		this.name = name;
		this.joinCode = joinCode;
	}
}
