package com.lazyloris.mcp.application.jsf;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lazyloris.mcp.application.model.repository.Repository;

@Component
@Scope("session")
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String password;
	private String error = null;
	
	@Autowired
	private Repository repository;

	public LoginBean() {
	}

	public LoginBean(String name, String password) {
		this.setName(name);
		this.setPassword(password);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String authenticate() {
		if (getName().startsWith("Jim")) {
			setError("Sorry, no Jims are allowed");
			return null;
		} else {
			setError(null);
			return "welcome";
		}
	}
}
