package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
//@IdClass(PictureSubmissionId.class)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PictureSubmission extends Submission {

	@ManyToOne
	@JoinColumn(name = "picture_id")
	private Picture picture;

	public PictureSubmission(Team team, Picture picture, MultipartFile file) throws IOException {
		super(team, file);
		this.picture = picture;
	}
}
