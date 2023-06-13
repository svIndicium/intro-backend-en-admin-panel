package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileSubmission {

	private String type;

	@Lob
	private byte[] content;

	public FileSubmission(MultipartFile file) throws IOException {
		this.type = file.getContentType();
		this.content = file.getBytes();
	}
}
