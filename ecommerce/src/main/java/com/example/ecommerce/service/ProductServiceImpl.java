package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repo.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	private final ProductRepository repo;

	public ProductServiceImpl(ProductRepository r) {
		repo = r;
	}

	public Product create(Product p) {
		p.setId(null);
		return repo.save(p);
	}

	@Transactional(readOnly = true)
	public List<Product> getAll() {
		return repo.findAll();
	}

	@Transactional(readOnly = true)
	public Product getById(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
	}

	public Product update(Long id, Product p) {
		Product x = getById(id);
		x.setName(p.getName());
		x.setDescription(p.getDescription());
		x.setPrice(p.getPrice());
		x.setStock(p.getStock());
		return repo.save(x);
	}

	public void delete(Long id) {
		if (!repo.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
		repo.deleteById(id);
	}
}