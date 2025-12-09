package com.carrental.repository;

import com.carrental.entity.BookingEntity;
import com.carrental.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findByUserOrderByCreatedAtDesc(UserEntity user);

     @Query("SELECT b FROM BookingEntity b WHERE b.status IN ('CONFIRMED', 'ACTIVE')")
     List<BookingEntity> findActiveBookings();
}
