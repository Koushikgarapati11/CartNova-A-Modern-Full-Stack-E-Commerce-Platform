package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.model.CustomerOrder;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://127.0.0.1:5173",
		"http://127.0.0.1:5174" })
public class OrderController {
	private final OrderService s;

	public OrderController(OrderService s) {
		this.s = s;
	}

	@PostMapping("/cart")
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerOrder cart(@Valid @RequestBody CartRequest r) {
		return s.addToCart(r);
	}

	@GetMapping("/cart/{customerId}")
	public List<CustomerOrder> cart(@PathVariable Long customerId) {
		return s.getCart(customerId);
	}

	@PostMapping("/checkout/{orderId}")
	public CustomerOrder checkout(@PathVariable Long orderId) {
		return s.checkout(orderId);
	}

	@GetMapping
	public List<CustomerOrder> all() {
		return s.getAll();
	}

	@GetMapping("/customer/{customerId}")
	public List<CustomerOrder> customerOrders(@PathVariable Long customerId) {
		return s.getCustomerOrders(customerId);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		s.delete(id);
	}
}