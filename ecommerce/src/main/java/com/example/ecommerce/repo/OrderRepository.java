package com.example.ecommerce.repo;

import com.example.ecommerce.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
	List<CustomerOrder> findByCustomerIdAndStatus(Long customerId, String status);

	List<CustomerOrder> findByCustomerId(Long customerId);
}