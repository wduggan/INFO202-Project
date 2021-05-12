/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author dugwi731
 */
public class Customer {
	
	private Integer customerID;
	private String username;
	private String firstName;
	private String surName;
	private String password;
	private String emailAddress;
	private String shippingAddress;
	
	public Customer() {
		
	}
	
	public Customer (Integer customerID, String userName, String firstname, String surname, String email, String shippingAddress, String password) {
		this.customerID = customerID;
		this.username = userName;
		this.firstName = firstname;
		this.surName = surname;
		this.emailAddress = email;
		this.shippingAddress = shippingAddress;
		this.password = password;
	}

	public Integer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surName;
	}

	public void setSurname(String surName) {
		this.surName = surName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		return "Customer{" + "customerID=" + customerID + ", username=" + username + ", firstName=" + firstName + ", surName=" + surName + ", password=" + password + ", emailAddress=" + emailAddress + ", shippingAddress=" + shippingAddress + '}';
	}
	
	
	
}
