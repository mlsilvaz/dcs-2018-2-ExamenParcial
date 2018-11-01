package dycs.common.application;

public interface UnitOfWork {
	public boolean beginTransaction();
	public void commit(boolean commit);
	public void rollback(boolean rollback);
}
