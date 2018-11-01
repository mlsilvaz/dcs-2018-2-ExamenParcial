package dycs.common.application.dto;

import java.util.List;
import dycs.common.application.notification.Error;

public class ApiErrorDto {
	private List<Error> errors;
	
	public ApiErrorDto() {
	}
	
	public ApiErrorDto(List<Error> errors) {
		this.errors = errors;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
}
