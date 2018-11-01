package dycs.common.application.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Notification {
	private List<Error> errors = new ArrayList<>();

    public void addError(String message) {
        errors.add(new Error(message));
    }

    public String errorMessage() {
        return errors.stream().map(e -> e.getMessage()).collect(Collectors.joining(", "));
    }
    
    public String errorMessage(String separator) {
        return errors.stream().map(e -> e.getMessage()).collect(Collectors.joining(separator));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
