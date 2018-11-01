package dycs.producto.application.deserializer;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.modelmapper.jackson.JsonNodeValueReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dycs.producto.application.dto.ProductoCreateDto;

public class ProductoCreateDeserializer extends JsonDeserializer<ProductoCreateDto> {
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public ProductoCreateDto deserialize(JsonParser jsonParser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ProductoCreateDto productoCreateDto = null;
		String json = jsonParser.readValueAsTree().toString();
		JsonNode node = new ObjectMapper().readTree(json);
		modelMapper.getConfiguration()
		  .addValueReader(new JsonNodeValueReader());
		try {
		  productoCreateDto = modelMapper.map(node, ProductoCreateDto.class);
		} catch(Exception ex) {
			productoCreateDto = new ProductoCreateDto();
		}
		return productoCreateDto;
	}
}
