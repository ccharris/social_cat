package com.harris.carolyn.lab302.beans;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Email {

	@Size(min = 2)
	private String message;
	@NotEmpty
	private String address;
	private String subject;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
}