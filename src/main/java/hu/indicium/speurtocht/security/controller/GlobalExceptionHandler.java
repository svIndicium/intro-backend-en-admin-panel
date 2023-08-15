package hu.indicium.speurtocht.security.controller;

import hu.indicium.speurtocht.service.exceptions.AlreadyApprovedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	ProblemDetail handleException(Exception e) {
		log.error("An unknown error has occurred.", e);
		log.info(e.getLocalizedMessage());

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		problemDetail.setTitle("An unknown error has occurred");
//		problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
		return problemDetail;
	}

	@ExceptionHandler(ExpiredJwtException.class)
	ProblemDetail handleJwtExpiredException(ExpiredJwtException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Session expired");
		problemDetail.setTitle("Session expired");
//		problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
		return problemDetail;
	}

	@ExceptionHandler(EntityNotFoundException.class)
	ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
		log.error("An entity was not found.", e);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
		problemDetail.setTitle("Entity not found");
//		problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
		return problemDetail;
	}

	@ExceptionHandler(AlreadyApprovedException.class)
	ProblemDetail handleAlreadyApprovedException(AlreadyApprovedException e) {
		log.warn("User tries to submit to a locked submission");

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.LOCKED, "Submission is in a locked state (pending or approved)");
		problemDetail.setTitle("Submission is locked");
//		problemDetail.setType(URI.create("https://api.bookmarks.com/errors/not-found"));
		return problemDetail;
	}
}
