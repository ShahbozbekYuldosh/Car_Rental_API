package com.carrental.repository;

import com.carrental.entity.CarEntity;
import com.carrental.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Boolean existsByBookingId(Long bookingId);
    List<ReviewEntity> findByCarAndApprovedTrueOrderByCreatedAtDesc(CarEntity car);
}
