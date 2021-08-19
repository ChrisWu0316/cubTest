package app.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="currentprice")
public class CurrentPriceBean {
	
	@Id
    private String code;
    private String symbol;
    private String rate;
    private String description;
    private double rate_float;
    private String updated;
    
    @Override
    public String toString() {
        return String.format("CurrentPriceBean[id=%d, name='%s', address='%s']", 
        		code, symbol, rate, description);
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRate_float() {
		return rate_float;
	}

	public void setRate_float(double rate_float) {
		this.rate_float = rate_float;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

    
    
}
