package app.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="currentcode")
public class CurrentCodeBean {

	@Id
    private String current;
    private String country;
    
    @Override
    public String toString() {
        return String.format("CurrentCodeBean[id=%d, name='%s', address='%s']", 
        		current, country);
    }

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
    
    
}
