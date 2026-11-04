package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.model.CustomerOrder;
import java.util.List;

public interface OrderService {
	CustomerOrder addToCart(CartRequest r);

	List<CustomerOrder> getCart(Long customerId);

	CustomerOrder checkout(Long orderId);

	List<CustomerOrder> getAll();

	List<CustomerOrder> getCustomerOrders(Long customerId);

	void delete(Long id);
}