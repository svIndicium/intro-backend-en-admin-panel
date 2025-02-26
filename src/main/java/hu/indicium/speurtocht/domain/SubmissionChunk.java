package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@IdClass(SubmissionChunkId.class)
public class SubmissionChunk {

    @Id
    @ManyToOne(optional = false)
//    @JoinColumns({
//            @JoinColumn(name = "attempt_team_id", referencedColumnName = "team_id", nullable = false),
//            @JoinColumn(name = "attempt_challenge_id", referencedColumnName = "challenge_id", nullable = false),
//            @JoinColumn(name = "attempt_id", referencedColumnName = "id", nullable = false)
//    })
    private SubmissionAttempt attempt;

    @Id
    private String fileName;

    @Id
    private Long id;

    @OneToOne
    private FileSubmission chunk;
}
