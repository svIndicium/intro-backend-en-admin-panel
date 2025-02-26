package hu.indicium.speurtocht.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@IdClass(SubmissionAttemptId.class)
public class SubmissionAttempt {
    @ManyToOne
    @Id
    private Team team;

    @ManyToOne
    @Id
    private Challenge challenge;

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private Map<String, Integer> files;

    public SubmissionAttempt(Team team, Challenge challenge, Map<String, Integer> files) {
        this.team = team;
        this.challenge = challenge;
        this.files = files;
    }
}
