package hu.indicium.speurtocht.domain;

import jakarta.persistence.Embedded;
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
public class Picture {
	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private Coordinate coordinate;

	public Picture(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
}
