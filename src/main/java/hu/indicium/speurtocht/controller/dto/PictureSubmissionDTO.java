package hu.indicium.speurtocht.controller.dto;

import hu.indicium.speurtocht.domain.SubmissionState;

public record PictureSubmissionDTO(Long id, SubmissionState state) {
}
