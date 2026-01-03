package com.carrental.repository;

import com.carrental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT COUNT(*) FROM bookings " +
            "WHERE car_id = :carId " +
            "AND status NOT IN ('CANCELLED') " +
            "AND (:start < drop_off_time AND :end > pick_up_time)", nativeQuery = true)
    long countOverlappingBookings(@Param("carId") Long carId,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

    @Query(value = "SELECT * FROM bookings WHERE user_id = :userId ORDER BY created_at DESC", nativeQuery = true)
    List<Booking> findAllByUserIdNative(@Param("userId") Long userId);
}