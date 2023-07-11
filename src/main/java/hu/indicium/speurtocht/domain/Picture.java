package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
	private PictureFile file;

	@Embedded
	private Coordinate coordinate;

	public Picture(Coordinate coordinate, MultipartFile multipartFile) throws IOException {
		this.coordinate = coordinate;
		this.file = new PictureFile(multipartFile);
	}
}
