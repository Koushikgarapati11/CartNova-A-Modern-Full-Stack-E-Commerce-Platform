package com.example.ecommerce.controller;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://127.0.0.1:5173",
		"http://127.0.0.1:5174" })
public class CustomerController {
	private final CustomerService s;

	public CustomerController(CustomerService s) {
		this.s = s;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer create(@Valid @RequestBody Customer c) {
		return s.create(c);
	}

	@GetMapping
	public List<Customer> all() {
		return s.getAll();
	}

	@GetMapping("/{id}")
	public Customer one(@PathVariable Long id) {
		return s.getById(id);
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @Valid @RequestBody Customer c) {
		return s.update(id, c);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		s.delete(id);
	}
}