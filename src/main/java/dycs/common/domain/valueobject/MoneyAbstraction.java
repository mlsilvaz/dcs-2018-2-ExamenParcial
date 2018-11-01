package dycs.common.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import dycs.ApplicationContextHolder;

public abstract class MoneyAbstraction {
	protected BigDecimal amount;
	protected Currency currency;
	protected static final int[] cents = new int[] { 1, 10, 100, 1000 };
	
	protected int roundingMode = RoundingMode.HALF_EVEN.ordinal();
	
	public abstract MoneyAbstraction add(MoneyAbstraction other);
	public abstract MoneyAbstraction subtract(MoneyAbstraction other);
	protected abstract MoneyAbstraction newMoney(double amount);
	public abstract MoneyAbstraction multiply(double amount);
	public abstract MoneyAbstraction multiply(BigDecimal amount);
	public abstract MoneyAbstraction multiply(BigDecimal amount, int roundingMode);
	public abstract boolean isNull();
	
	public MoneyNull getMoneyNull() {
		return ApplicationContextHolder.getContext().getBean(MoneyNull.class);
	}
    
	public BigDecimal getAmount() {
    	return this.amount;
	}
	
	protected long amount() {
    	return Math.round(amount.doubleValue() * centFactor());
    }
	
	public Currency getCurrency() {
		return currency;
	}
	
	public String getCurrencyAsString() {
		return currency.getCurrencyCode();
	}
	
	protected int centFactor() {
        return cents[currency.getDefaultFractionDigits()];
	}
	
	protected boolean sameCurrency(MoneyAbstraction other) {
    	return currency == other.currency;
    }
	
	public int compareTo(Object other) {
        return compareTo((MoneyAbstraction) other);
    }

    public int compareTo(MoneyAbstraction other) {
        if (amount() < other.amount()) {
            return -1;
        } else if (amount() == other.amount()) {
            return 0;
        } else {
            return 1;
        }
    }
    
    public boolean greaterThan(Money other) {
        return (compareTo(other) > 0);
    }
    
    public boolean equals(Object other) {
        return (other instanceof MoneyAbstraction) && equals((MoneyAbstraction) other);
    }
    
    public boolean equals(MoneyAbstraction other) {
        return currency.equals(other.currency) && (amount() == other.amount());
    }
    
    public int hashCode() {
        return (int) (amount() ^ (amount() >>> 32));
    }
}
