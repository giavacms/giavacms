package org.giavacms.paypal.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PaypalConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String accountEmail;
	private String testUrl;
	private String ufficialUrl;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public String getUfficialUrl() {
		return ufficialUrl;
	}

	public void setUfficialUrl(String ufficialUrl) {
		this.ufficialUrl = ufficialUrl;
	}

}
