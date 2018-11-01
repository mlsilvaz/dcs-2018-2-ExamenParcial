package dycs.common.domain.valueobject;

import java.math.BigDecimal;
import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class MoneyNull extends MoneyAbstraction {
	public MoneyNull() {
		this.amount = new BigDecimal(0);
		this.currency = Currency.getInstance("USD");
    }

	@Override
	public MoneyAbstraction add(MoneyAbstraction other) {
		return this;
	}

	@Override
	public MoneyAbstraction subtract(MoneyAbstraction other) {
		return this;
	}

	@Override
	protected MoneyAbstraction newMoney(double amount) {
		return this;
	}

	@Override
	public MoneyAbstraction multiply(double amount) {
		return this;
	}

	@Override
	public MoneyAbstraction multiply(BigDecimal amount) {
		return this;
	}

	@Override
	public MoneyAbstraction multiply(BigDecimal amount, int roundingMode) {
		return this;
	}

	@Override
	public boolean isNull() {
		return true;
	}
}
