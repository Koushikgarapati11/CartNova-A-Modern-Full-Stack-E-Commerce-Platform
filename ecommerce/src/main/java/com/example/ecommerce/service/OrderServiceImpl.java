package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repo.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orders;
    private final ProductRepository products;
    private final CustomerRepository customers;

    public OrderServiceImpl(
            OrderRepository o,
            ProductRepository p,
            CustomerRepository c) {

        orders = o;
        products = p;
        customers = c;
    }

    @Override
    public CustomerOrder addToCart(CartRequest r) {

        Customer c = customers.findById(r.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        Product p = products.findById(r.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        if (p.getStock() < r.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Not enough stock");
        }

        CustomerOrder o = new CustomerOrder();

        o.setCustomer(c);
        o.setProduct(p);
        o.setQuantity(r.getQuantity());

        o.setTotalAmount(
                p.getPrice().multiply(BigDecimal.valueOf(r.getQuantity()))
        );

        o.setStatus("CART");

        return orders.save(o);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrder> getCart(Long id) {

        return orders.findByCustomerIdAndStatus(id, "CART");
    }

    @Override
    public CustomerOrder checkout(Long id) {

        CustomerOrder o = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cart item not found"));

        if (!"CART".equals(o.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Item is not in cart");
        }

        Product p = products.findById(o.getProduct().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        if (p.getStock() < o.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Not enough stock");
        }

        p.setStock(p.getStock() - o.getQuantity());

        products.save(p);

        o.setStatus("PLACED");

        return orders.save(o);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrder> getAll() {

        return orders.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrder> getCustomerOrders(Long id) {

        return orders.findByCustomerId(id);
    }

    @Override
    public void delete(Long id) {

        CustomerOrder o = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        if (!"CART".equals(o.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only cart items can be deleted");
        }

        orders.delete(o);
    }
}