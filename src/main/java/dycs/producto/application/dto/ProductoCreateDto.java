package dycs.producto.application.dto;

import java.math.BigDecimal;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import dycs.accounts.api.application.deserializer.BankAccountCreateDeserializer;

//@JsonDeserialize(using = BankAccountCreateDeserializer.class)
public class ProductoCreateDto {
	private String number;
	private BigDecimal balance;
	private String currency;
	private boolean locked = false;
	private long customerId;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
}
