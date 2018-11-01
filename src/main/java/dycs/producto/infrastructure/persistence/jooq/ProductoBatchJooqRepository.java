package dycs.producto.infrastructure.persistence.jooq;

import java.util.List;
import javax.transaction.Transactional;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import dycs.producto.domain.entity.Producto;
import dycs.producto.domain.repository.ProductoBatchRepository;

@Transactional(rollbackOn=Exception.class)
@Repository
public class ProductoBatchJooqRepository implements ProductoBatchRepository {
	@Autowired
	DSLContext dsl;
	
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	protected String HIBERNATE_JDBC_BATCH_SIZE;
	
	public ProductoBatchJooqRepository() {
	}
	
	public void createBatch(List<Producto> productoList) {
		String sql = "INSERT INTO producto(number, balance, currency, locked, customer_id) VALUES(?, ?, ?, ?, ?)";
		BatchBindStep batch = dsl.batch(sql);
		long i = 0;
		for (Producto producto : productoList) {
			if (i % Integer.valueOf(HIBERNATE_JDBC_BATCH_SIZE) == 0 && i > 0) {
				batch.execute();
				batch = dsl.batch(sql);
			}
		    batch.bind(producto.getNumber(), producto.getBalance().getAmount(), producto.getBalance().getCurrency().getCurrencyCode(), producto.getIsLocked(), producto.getCustomer().getId());
		    i++;
		}
		batch.execute();
	}
}
