package com.example.ecommerce.dto;

import jakarta.validation.constraints.*;

public class CartRequest {
	@NotNull
	private Long customerId;
	@NotNull
	private Long productId;
	@NotNull
	@Min(1)
	private Integer quantity;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long v) {
		customerId = v;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long v) {
		productId = v;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer v) {
		quantity = v;
	}
}