package hu.indicium.speurtocht.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PictureFile {

	@Id
	@GeneratedValue
	private UUID id;

	private String type;

	@Lob
	private byte[] content;

	public PictureFile(String type, byte[] content) {
		this.type = type;
		this.content = content;
	}
}
