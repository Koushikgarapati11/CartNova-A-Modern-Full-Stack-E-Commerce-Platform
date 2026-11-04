package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(nullable = false)
	private String name;
	@NotBlank
	@Email
	@Column(nullable = false, unique = true)
	private String email;
	@NotBlank
	@Column(nullable = false)
	private String phone;

	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public String getName() {
		return name;
	}

	public void setName(String v) {
		name = v;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String v) {
		email = v;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String v) {
		phone = v;
	}
}