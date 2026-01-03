package com.carrental.repository;

import com.carrental.entity.Payment;
import com.carrental.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}