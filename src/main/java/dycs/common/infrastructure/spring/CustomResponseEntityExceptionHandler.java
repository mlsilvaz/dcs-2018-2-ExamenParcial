package dycs.common.infrastructure.spring;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dycs.common.application.dto.ApiErrorDto;
import dycs.common.application.notification.Error;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(UnexpectedRollbackException.class)
	public final ResponseEntity<Object> handleUncaughtException(Exception ex, WebRequest request) {
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public final ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return getResponse(HttpStatus.BAD_REQUEST);
	}
	
	@Override
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<Object> getResponse(HttpStatus httpStatus) {
		ApiErrorDto apiErrorDto = new ApiErrorDto();
		List<Error> errors = new ArrayList<Error>();
		String message = httpStatus.name().replace("_", " ");
		message = WordUtils.capitalizeFully(message);
		errors.add(new Error(message));
		apiErrorDto.setErrors(errors);
		return new ResponseEntity<>(apiErrorDto, httpStatus);
	}
}
