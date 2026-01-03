package com.carrental.repository;

import com.carrental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT SUM(total_amount) FROM bookings WHERE status = 'COMPLETED'", nativeQuery = true)
    Double getTotalRevenue();

    @Query(value = "SELECT status, COUNT(*) FROM bookings GROUP BY status", nativeQuery = true)
    List<Object[]> getBookingStatsNative();

    @Query(value = "SELECT cv.model, COUNT(b.id) as count FROM bookings b " +
            "JOIN cars c ON b.car_id = c.id " +
            "JOIN car_variants cv ON c.variant_id = cv.id " +
            "GROUP BY cv.model ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getTop5CarsNative();
}