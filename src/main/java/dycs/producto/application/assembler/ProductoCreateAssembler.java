package dycs.producto.application.assembler;

import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dycs.producto.application.dto.ProductoCreateDto;
import dycs.producto.domain.entity.Producto;
import dycs.common.domain.valueobject.Money;
import dycs.common.domain.valueobject.MoneyAbstraction;
import dycs.common.infrastructure.persistence.hibernate.UnitOfWorkHibernate;
import dycs.customers.domain.entity.Customer;

@Component
public class ProductoCreateAssembler {
	@Autowired
	protected UnitOfWorkHibernate unitOfWork;
	
	public Producto toEntity(ProductoCreateDto productoCreateDto) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(getConverter());
		Producto producto = modelMapper.map(productoCreateDto, Producto.class);
		return producto;
	}
	
	public List<Producto> toEntityList(List<ProductoCreateDto> productoCreateListDto) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addConverter(getConverter());
		List<Producto> productoListDto = modelMapper.map(productoCreateListDto, new TypeToken<List<Producto>>() {}.getType());
		return productoListDto;
	}
	
	private Converter<ProductoCreateDto, Producto> getConverter() {
		Converter<ProductoCreateDto, Producto> converter = new Converter<ProductoCreateDto, Producto>() {
		    @Override
		    public Producto convert(MappingContext<ProductoCreateDto, Producto> context) {
		    	ProductoCreateDto productoCreateDto =  ProductoCreateDto.class.cast(context.getSource());
		    	MoneyAbstraction balance = new Money(productoCreateDto.getBalance(), productoCreateDto.getCurrency());
		        Producto producto = new Producto();
		        producto.setNumber(productoCreateDto.getNumber());
		        producto.setBalance(balance);
		        Customer customer = new Customer();
		        customer.setId(productoCreateDto.getCustomerId());
		        producto.setCustomer(customer);
		    	return producto;
		    }
		};
		return converter;
	}
	
	public ProductoCreateDto toDto(Producto producto) {
		ModelMapper modelMapper = new ModelMapper();
		PropertyMap<Producto, ProductoCreateDto> map = new PropertyMap<Producto, ProductoCreateDto>() {
		  protected void configure() {
			map().setNumber(source.getNumber());
		    map().setBalance(source.getBalance().getAmount());
		    map().setCurrency(source.getBalance().getCurrencyAsString());
		    map().setLocked(source.getIsLocked());
		    map().setCustomerId(source.getCustomer().getId());
		  }
		};
		modelMapper.addMappings(map);
		ProductoCreateDto productoCreateDto = modelMapper.map(producto, ProductoCreateDto.class);
		return productoCreateDto;
	}
}
