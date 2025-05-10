package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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

//	@Lob
//	private Blob content;  // Use Blob for large objects

	public FileSubmission(MultipartFile file) throws IOException {
		this.type = file.getContentType();
	}

	public FileSubmission(String type) {
		this.type = type;
	}

//	public FileSubmission(String type, InputStream content) {
//		this.type = type;
//		this.content = (Blob) content;
//	}
//
//	// Get an InputStream from the Blob
//	public InputStream getInputStream() throws IOException, SQLException {
//		return content.getBinaryStream();
//	}
}
