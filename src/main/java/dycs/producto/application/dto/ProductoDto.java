package dycs.producto.application.dto;

import java.math.BigDecimal;

public class ProductoDto {
	private BigDecimal balance;
	private String currency;
	
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
}
