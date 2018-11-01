package dycs.common.infrastructure.persistence.hibernate;

import java.sql.SQLException;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional(rollbackOn=Exception.class)
public class BaseHibernateRepository<T> {
	@Autowired
	protected UnitOfWorkHibernate unitOfWork;
	
	private final Class<T> type;
		
	public BaseHibernateRepository(Class<T> type) {
		this.type = type;
	}
	
	public void create(T entity) throws SQLException {
		saveOrUpdate(entity);
    }
	
	public T read(long id) throws SQLException {
		if (unitOfWork.isSpringAOPTransaction()) {
			return unitOfWork.getSession().get(type, id);
		} else {
			boolean status = unitOfWork.beginTransaction();
			T entity = unitOfWork.getSession().get(type, id);
			unitOfWork.commit(status);
			return entity;
		}
	}
	
	public void update(T entity) throws SQLException {
		saveOrUpdate(entity);
    }
	
	public void delete(T entity) throws SQLException {
		if (unitOfWork.isSpringAOPTransaction()) {
			unitOfWork.getSession().delete(entity);
		} else {
			boolean status = unitOfWork.beginTransaction();
			unitOfWork.getSession().delete(entity);
			unitOfWork.commit(status);
		}
    }
	
	private void saveOrUpdate(T entity) throws SQLException {
		if (unitOfWork.isSpringAOPTransaction()) {
			unitOfWork.getSession().saveOrUpdate(entity);
		} else {
			boolean status = unitOfWork.beginTransaction();
			unitOfWork.getSession().saveOrUpdate(entity);
			unitOfWork.commit(status);
		}
	}
}
