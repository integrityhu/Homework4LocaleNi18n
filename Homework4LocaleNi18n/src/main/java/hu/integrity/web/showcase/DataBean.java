package hu.integrity.web.showcase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DataBean {
	@Id
	@GeneratedValue
	private long id;

	private String key;

	public long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
