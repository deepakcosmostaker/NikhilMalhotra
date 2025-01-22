package com.cosmostaker.razorpay_integration.repository;

import com.cosmostaker.razorpay_integration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
