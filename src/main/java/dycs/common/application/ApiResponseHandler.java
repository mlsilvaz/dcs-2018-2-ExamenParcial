package dycs.common.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dycs.common.application.dto.ApiErrorDto;
import dycs.common.application.dto.ApiStringResponseDto;
import dycs.common.application.notification.Error;

@Component
public class ApiResponseHandler {
	public ApiStringResponseDto getApplicationMessage(String message) {
		ApiStringResponseDto apiStringResponseDto = new ApiStringResponseDto();
		apiStringResponseDto.setMessage(message);
		return apiStringResponseDto;
	}
	
	public ApiErrorDto getApplicationError(String errorMessages) {
		ApiErrorDto apiErrorDto = new ApiErrorDto();
		List<Error> errorList = new ArrayList<Error>();
		String[] errors = errorMessages.split(",");
		for (String error : errors) {
			errorList.add(new Error(error));
        }
		apiErrorDto.setErrors(errorList);
		return apiErrorDto;
	}
	
	public ApiErrorDto getApplicationException() {
		ApiErrorDto apiErrorDto = new ApiErrorDto();
		List<Error> errorList = new ArrayList<Error>();
		errorList.add(new Error("Internal Server Error"));
		apiErrorDto.setErrors(errorList);
		return apiErrorDto;
	}
	
	public void throwTestException() throws Exception {
		throw new Exception("test");
	}
}
