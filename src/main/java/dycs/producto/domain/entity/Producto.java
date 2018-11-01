package dycs.producto.domain.entity;

import dycs.common.application.notification.Notification;
import dycs.common.domain.valueobject.MoneyAbstraction;
import dycs.customers.domain.entity.Customer;

public class Producto {
	private long id;
	private String number;
	private MoneyAbstraction balance;
	private boolean isLocked;
	private Customer customer;
	
	public Producto() {
    }

    public void withdrawMoney(MoneyAbstraction amount) {
    	Notification notification = this.withdrawValidation(amount);
        if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }
        this.balance = this.balance.subtract(amount);
    }

    public void depositMoney(MoneyAbstraction amount) {
    	Notification notification = this.depositValidation(amount);
        if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }
        this.balance = this.balance.add(amount);
    }
    
    public Notification withdrawValidation(MoneyAbstraction amount) {
    	Notification notification = new Notification();
        this.validateAmount(notification, amount);
        this.validateProducto(notification);
        this.validateBalance(notification, amount);
        return notification;
    }
    
    public Notification depositValidation(MoneyAbstraction amount) {
        Notification notification = new Notification();
        this.validateAmount(notification, amount);
        this.validateProducto(notification);
        return notification;
    }
    
    private void validateAmount(Notification notification, MoneyAbstraction amount) {
        if (amount == null || amount.getAmount() == null) {
            notification.addError("amount is missing");
            return;
        }
        if (amount.getAmount().doubleValue() <= 0.0) {
            notification.addError("The amount must be greater than zero");
        }
    }
    
    private void validateProducto(Notification notification) {
        if (!this.hasIdentity()) {
            notification.addError("The product has no identity");
        }
        if (this.isLocked) {
        	notification.addError("The product is locked");
        }
    }
    
    private void validateBalance(Notification notification, MoneyAbstraction amount) {
        if (this.balance == null) {
            notification.addError("balance cannot be null");
        }
        if (!this.canBeWithdrawed(amount)) {
        	notification.addError("Cannot withdraw in the account, amount is greater than balance");
        }
    }

    public boolean canBeWithdrawed(MoneyAbstraction amount) {
        return !this.isLocked && (this.balance.compareTo(amount) >= 0);
    }
    
    public void lock() {
        if (!this.isLocked) {
            this.isLocked = true;
        }
    }

    public void unLock() {
        if (this.isLocked) {
            this.isLocked = false;
        }
    }

    public boolean hasIdentity() {
        return this.id > 0 && !this.number.trim().equals("");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public MoneyAbstraction getBalance() {
        return this.balance;
    }

    public void setBalance(MoneyAbstraction balance) {
        this.balance = balance;
    }

    public boolean getIsLocked() {
        return isLocked;
    }
    
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
