package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileSubmission {

	@Id
	@GeneratedValue
	private UUID id;

	private String type;

	@Lob @Basic(fetch = FetchType.LAZY)
	private byte[] content;

	public FileSubmission(MultipartFile file) throws IOException {
		this.type = file.getContentType();
		this.content = file.getBytes();
	}

	public FileSubmission(String type, MultipartFile file) throws IOException {
		this.type = type;
		this.content = file.getBytes();
	}

	public FileSubmission(String type, byte[] content) {
		this.type = type;
		this.content = content;
	}
}
