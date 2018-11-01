package dycs.common.infrastructure.persistence.hibernate;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import dycs.common.application.UnitOfWork;

@Transactional
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UnitOfWorkHibernate implements UnitOfWork {
	@Autowired
	protected DataSource dataSource;
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	private Session session;
	
	private Transaction transaction;
	
	private boolean springAOPTransaction = true;
		
	public boolean isSpringAOPTransaction() {
		return springAOPTransaction;
	}
	
	public Session getSession() {
		if (this.transaction == null) {
			this.springAOPTransaction = true;
			return sessionFactory.getCurrentSession();
		} else {
			this.springAOPTransaction = false;
			return session;
		}
	}
	
	public boolean beginTransaction() {
		if (this.transaction == null || !this.transaction.isActive()) {
			this.session = sessionFactory.openSession();
			this.transaction = this.session.beginTransaction();
			return true;
		}
		return false;
	}
	
	public void commit(boolean commit) {
		if (this.transaction != null && this.transaction.isActive() && commit) {
			this.transaction.commit();
			this.session.close();
		}
    }
	
	public void rollback(boolean rollback) {
        if (this.transaction != null && this.transaction.isActive() && rollback) {
        	this.transaction.rollback();
        	this.session.close();
        }
    }
}
