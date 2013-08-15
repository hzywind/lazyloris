package com.lazyloris.mcp.application.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.stereotype.Component;

@Component
@ManagedBean
@SessionScoped
public class LoginBean {
	private String name;
	private String password;
	private String error = null;

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
			return "authenticated";
		}
	}
}
