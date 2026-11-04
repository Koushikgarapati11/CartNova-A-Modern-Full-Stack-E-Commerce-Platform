package com.example.ecommerce.service;

import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repo.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    public CustomerServiceImpl(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public Customer create(Customer c) {
        c.setId(null);
        return repo.save(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"
                    )
                );
    }

    @Override
    public Customer update(Long id, Customer c) {
        Customer existingCustomer = getById(id);

        existingCustomer.setName(c.getName());
        existingCustomer.setEmail(c.getEmail());
        existingCustomer.setPhone(c.getPhone());

        return repo.save(existingCustomer);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer not found"
            );
        }

        repo.deleteById(id);
    }
}