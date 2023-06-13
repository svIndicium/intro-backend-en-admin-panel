package hu.indicium.speurtocht.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@ToString
public class Coordinate {
	private float latitude;
	private float longitude;
}
