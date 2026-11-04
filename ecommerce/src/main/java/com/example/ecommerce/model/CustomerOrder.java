package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id")
	private Product product;
	@Min(1)
	@Column(nullable = false)
	private Integer quantity;
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal totalAmount;
	@Column(nullable = false)
	private String status = "CART";
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public CustomerOrder() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long v) {
		id = v;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer v) {
		customer = v;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product v) {
		product = v;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer v) {
		quantity = v;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal v) {
		totalAmount = v;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String v) {
		status = v;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime v) {
		createdAt = v;
	}
}