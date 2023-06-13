package hu.indicium.speurtocht.controller.dto;

import hu.indicium.speurtocht.domain.SubmissionState;

import java.util.List;

public record PictureDTO(Long id, List<SubmissionState> submissions) {}
