package dycs.producto.domain.repository;

import java.sql.SQLException;

import dycs.producto.domain.entity.Producto;

//Separated Interface Pattern
//https://www.martinfowler.com/eaaCatalog/separatedInterface.html
public interface ProductoRepository {
	public void create(Producto producto) throws SQLException;
	public void delete(Producto producto) throws SQLException;
	public Producto read(long id) throws SQLException;
}
