package dycs.common.domain.valueobject;

import java.math.BigDecimal;
import java.util.Currency;

public class Money extends MoneyAbstraction {
	public Money() {
	}
    
    public Money(double amount, String currency) {
        this.currency = Currency.getInstance(currency);
        long longAmount = Math.round(amount * centFactor());
		this.amount = BigDecimal.valueOf(longAmount, this.currency.getDefaultFractionDigits());
    }
    
    public Money(BigDecimal amount, String currency) {
    	this.currency = Currency.getInstance(currency);
        this.amount = amount;
    }

    public Money(BigDecimal amount, Currency currency, int roundingMode) {
        this.currency = currency;
        this.roundingMode = roundingMode;
        this.amount = amount;
    }
       
    public static Money dollars(double amount) {
        return new Money(amount, "USD");
    }
    
    public static Money soles(double amount) {
        return new Money(amount, "PEN");
    }
    
    public static Money euros(double amount) {
        return new Money(amount, "EUR");
    }
    
    @Override
    public MoneyAbstraction add(MoneyAbstraction other) {
    	if (!sameCurrency(other)) {
    		return getMoneyNull();
    	}
    	return newMoney(amount.doubleValue() + other.amount.doubleValue());
    }

    @Override
    public MoneyAbstraction subtract(MoneyAbstraction other) {
    	if (!sameCurrency(other)) {
    		return getMoneyNull();
    	}
    	return newMoney(amount.doubleValue() - other.amount.doubleValue());
    }
    
    protected MoneyAbstraction newMoney(double amount) {
        Money money = new Money(amount, this.currency.getCurrencyCode());
        return money;
    }
        
    @Override
    public MoneyAbstraction multiply(double amount) {
        return multiply(new BigDecimal(amount));
    }

    @Override
    public MoneyAbstraction multiply(BigDecimal amount) {
        return multiply(amount, roundingMode);
    }

    @Override
    public MoneyAbstraction multiply(BigDecimal amount, int roundingMode) {
        return new Money(this.amount.multiply(amount), currency, roundingMode);
    }
    
    @Override
    public boolean isNull() {
        return false;
    }
}
