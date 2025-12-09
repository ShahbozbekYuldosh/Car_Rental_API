package com.carrental.repository;

import com.carrental.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByTransactionId(String transactionId);
    Optional<PaymentEntity> findByBookingId(Long bookingId);
}
