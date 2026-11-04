package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import java.util.List;

public interface ProductService {
	Product create(Product p);

	List<Product> getAll();

	Product getById(Long id);

	Product update(Long id, Product p);

	void delete(Long id);
}