package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "http://127.0.0.1:5173",
		"http://127.0.0.1:5174" })
public class ProductController {
	private final ProductService s;

	public ProductController(ProductService s) {
		this.s = s;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Product create(@Valid @RequestBody Product p) {
		return s.create(p);
	}

	@GetMapping
	public List<Product> all() {
		return s.getAll();
	}

	@GetMapping("/{id}")
	public Product one(@PathVariable Long id) {
		return s.getById(id);
	}

	@PutMapping("/{id}")
	public Product update(@PathVariable Long id, @Valid @RequestBody Product p) {
		return s.update(id, p);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		s.delete(id);
	}
}