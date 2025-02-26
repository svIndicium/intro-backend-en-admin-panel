package hu.indicium.speurtocht.domain;

import java.io.Serializable;
import java.util.Objects;

public class SubmissionChunkId implements Serializable {
    private SubmissionAttempt attempt;
    private String fileName;
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmissionChunkId that = (SubmissionChunkId) o;
        return Objects.equals(attempt, that.attempt) && Objects.equals(fileName, that.fileName) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, fileName, id);
    }
}
