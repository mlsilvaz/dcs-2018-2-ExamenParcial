package dycs.producto.api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Isolation;
import dycs.producto.application.assembler.ProductoCreateAssembler;
import dycs.producto.application.dto.ProductoCreateDto;
import dycs.producto.application.validation.ProductoCreateValidation;
import dycs.producto.domain.entity.Producto;
import dycs.producto.domain.repository.ProductoBatchRepository;
import dycs.producto.domain.repository.ProductoRepository;
import dycs.common.application.ApiResponseHandler;
import dycs.common.application.UnitOfWork;
import dycs.common.domain.valueobject.Money;
import dycs.common.domain.valueobject.MoneyAbstraction;

@RestController
@RequestMapping("v1/customers/{customerId}/accounts")
public class ProductoController {
	@Autowired
	UnitOfWork unitOfWork;
	
	@Autowired
	ProductoRepository productoRepository;
	
	@Autowired
	ProductoBatchRepository productoBatchRepository;
	
	@Autowired
	ProductoCreateValidation productoCreateValidation;
	
	@Autowired
	ProductoCreateAssembler productoCreateAssembler;
	
	@Autowired
	ApiResponseHandler apiResponseHandler;
	
	@Transactional(rollbackFor=Exception.class, isolation = Isolation.READ_COMMITTED)
	@RequestMapping(
	    method = RequestMethod.POST,
	    path = "",
	    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
	    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
	)
	public ResponseEntity<Object> create(@PathVariable("customerId") long customerId, @RequestBody ProductoCreateDto productoCreateDto) throws Exception {
        try {
        	productoCreateDto.setCustomerId(customerId);
        	productoCreateValidation.validate(productoCreateDto);
            Producto producto = productoCreateAssembler.toEntity(productoCreateDto);
            productoRepository.create(producto);
            ProductoCreateDto productoCreateDto2 = productoCreateAssembler.toDto(producto);
            return new ResponseEntity<Object>(productoCreateDto2, HttpStatus.CREATED);
        } catch(IllegalArgumentException ex) {
        	ex.printStackTrace();
        	return new ResponseEntity<Object>(apiResponseHandler.getApplicationError(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>(apiResponseHandler.getApplicationException(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@RequestMapping(
	    method = RequestMethod.POST,
	    path = "/unit-of-work",
	    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
	    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
	)
	public ResponseEntity<Object> unitOfWork(@PathVariable("customerId") long customerId, @RequestBody List<ProductoCreateDto> productoCreateListDto) throws Exception {
		boolean status = false;
		try {
			for (ProductoCreateDto productoCreateDto : productoCreateListDto) {
				productoCreateDto.setCustomerId(customerId);
			}
			List<Producto> productoList = productoCreateAssembler.toEntityList(productoCreateListDto);
            status = unitOfWork.beginTransaction();
            long lastId = 0;
            for (Producto producto : productoList) {
            	productoRepository.create(producto);
            	lastId = producto.getId();
            }
            Producto producto = productoRepository.read(lastId);
        	productoRepository.delete(producto);
            unitOfWork.commit(status);
            return new ResponseEntity<Object>(apiResponseHandler.getApplicationMessage("Products were created!"), HttpStatus.CREATED);
        } catch(IllegalArgumentException ex) {
        	unitOfWork.rollback(status);
        	return new ResponseEntity<Object>(apiResponseHandler.getApplicationError(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
        	unitOfWork.rollback(status);
			return new ResponseEntity<Object>(apiResponseHandler.getApplicationException(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@RequestMapping(
	    method = RequestMethod.POST,
	    path = "/batch",
	    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
	    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
	)
	public ResponseEntity<Object> batch(@PathVariable("customerId") long customerId, @RequestBody List<ProductoCreateDto> productoCreateListDto) throws Exception {
		try {
			for (ProductoCreateDto productoCreateDto : productoCreateListDto) {
				productoCreateDto.setCustomerId(customerId);
			}
			List<Producto> productoList = productoCreateAssembler.toEntityList(productoCreateListDto);
			productoBatchRepository.createBatch(productoList);
            return new ResponseEntity<Object>(apiResponseHandler.getApplicationMessage("Bank Accounts were created!"), HttpStatus.CREATED);
        } catch(IllegalArgumentException ex) {
        	ex.printStackTrace();
        	return new ResponseEntity<Object>(apiResponseHandler.getApplicationError(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
        	ex.printStackTrace();
			return new ResponseEntity<Object>(apiResponseHandler.getApplicationException(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@RequestMapping(
		method = RequestMethod.POST,
		path = "/null-object",
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE
	)
	public ResponseEntity<Object> nullObject() throws Exception {
        try {
            Money money1 = Money.dollars(1850.74);
            Money money2 = Money.soles(1850.75);
            StringBuilder message = new StringBuilder();
            message.append("equals: " + money1.equals(money2));
            MoneyAbstraction money = money2.subtract(money1);
            message.append(" ** " + money.getCurrencyAsString() + ": " + money.getAmount());
            message.append(" ** " + (money.isNull() ? "Null Object - different currencies" : "Real Object"));
            return new ResponseEntity<Object>(apiResponseHandler.getApplicationMessage(message.toString()), HttpStatus.OK);
        } catch(IllegalArgumentException ex) {
        	ex.printStackTrace();
        	return new ResponseEntity<Object>(apiResponseHandler.getApplicationError(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>(apiResponseHandler.getApplicationException(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
