package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PictureFile thumbnail;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private PictureFile file;

	@Embedded
	private Coordinate coordinate;

	public Picture(Coordinate coordinate, PictureFile thumbnail, PictureFile original) {
		this.coordinate = coordinate;
		this.thumbnail = thumbnail;
		this.file = original;
	}
}
