package com.cosmostaker.razorpay_integration.repository;

import com.cosmostaker.razorpay_integration.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails, Long> {
//    Optional<OrderDetails> findByOrderId(String orderId);

    Optional<OrderDetails> findByOrderId(String orderId);
}
