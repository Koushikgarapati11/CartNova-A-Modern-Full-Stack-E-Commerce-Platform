package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import java.util.List;

public interface CustomerService {
	Customer create(Customer c);

	List<Customer> getAll();

	Customer getById(Long id);

	Customer update(Long id, Customer c);

	void delete(Long id);
}