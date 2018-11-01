package dycs.producto.application.validation;

import org.springframework.stereotype.Component;

import dycs.producto.application.dto.ProductoCreateDto;
import dycs.common.application.notification.Notification;

@Component
public class ProductoCreateValidation {
	public void validate(ProductoCreateDto productoCreateDto) {
		Notification notification = this.validateData(productoCreateDto);
		if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }
	}
	
	public Notification validateData(ProductoCreateDto productoCreateDto) {
		Notification notification = new Notification();
		if (productoCreateDto == null) {
			notification.addError("Missing Producto parameters");
			return notification;
		}
		if (productoCreateDto.getNumber().trim().isEmpty()) {
			notification.addError("Missing Number parameter");
		}
		if (productoCreateDto.getBalance() == null) {
			notification.addError("Missing Balance parameter");
			return notification;
		}
		if (productoCreateDto.getBalance().doubleValue() <= 0.0) {
			notification.addError("Balance must be grater than zero");
		}
		return notification;
	}
}
