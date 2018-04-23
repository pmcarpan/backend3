package com.onlinestore.backend.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty (message = "Please enter your first name")
	private String firstname;
	
	@NotEmpty (message = "Please enter your last name")
	private String lastname;
	
	@NotEmpty (message = "Please enter your email address")
	@Email (message = "Please enter your email address")
	private String email;
	
	@NotNull (message = "Phone number should contain atleast 10 digits")
	@Min(value = 1000000000, message = "Phone number should contain atleast 10 digits")
	private Long phone;
	
	@NotEmpty (message = "Please enter your address")
	private String address;
	
	@Id
	@Length (min = 5, message = "Username must contain atleast 5 characters")
	private String username;
	
	@Length (min = 8, message = "Password must contain atleast 8 characters")
	private String password;
	
	private String role;
	
	private boolean enabled;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	private Cart cart;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<OrderDetail> orders = new HashSet<>();
	
	public User() {}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Set<OrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(Set<OrderDetail> orders) {
		this.orders = orders;
	}

}
