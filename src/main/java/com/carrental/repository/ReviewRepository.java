package com.carrental.repository;

import com.carrental.entity.CarEntity;
import com.carrental.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Boolean existsByBookingId(Long bookingId);
    List<ReviewEntity> findByCarAndApprovedTrueOrderByCreatedAtDesc(CarEntity car);
    List<ReviewEntity> findApprovedReviewsByCarId(Long carId);
    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.car.id = :carId AND r.approved = true")
    Optional<Double> getAverageRatingByCarId(@Param("carId") Long carId);
    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.car.id = :carId AND r.approved = true")
    Long countApprovedReviewsByCarId(@Param("carId") Long carId);
}
