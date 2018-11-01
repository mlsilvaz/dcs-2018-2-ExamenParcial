package dycs.producto.domain.repository;

import java.util.List;

import dycs.producto.domain.entity.Producto;

public interface ProductoBatchRepository {
	public void createBatch(List<Producto> productoList);
}
