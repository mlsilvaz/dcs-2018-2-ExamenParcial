package dycs.producto.infrastructure.persistence.hibernate;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dycs.producto.domain.entity.Producto;
import dycs.producto.domain.repository.ProductoRepository;
import dycs.producto.infrastructure.persistence.jooq.ProductoBatchJooqRepository;
import dycs.common.infrastructure.persistence.hibernate.BaseHibernateRepository;

@Transactional(rollbackOn=Exception.class)
@Repository
public class ProductoHibernateRepository extends BaseHibernateRepository<Producto> implements ProductoRepository {
	@Autowired
	ProductoBatchJooqRepository productoBatchJooqRepository;
	
	public ProductoHibernateRepository() {
		super(Producto.class);
	}
}
