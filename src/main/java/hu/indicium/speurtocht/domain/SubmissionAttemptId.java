package hu.indicium.speurtocht.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubmissionAttemptId implements Serializable {
    private Team team;
    private Challenge challenge;
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionAttemptId that = (SubmissionAttemptId) o;
        return Objects.equals(team, that.team) && Objects.equals(challenge, that.challenge) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, challenge, id);
    }
}
