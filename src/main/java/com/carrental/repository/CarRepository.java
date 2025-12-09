package com.carrental.repository;

import com.carrental.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends  JpaRepository<CarEntity, Long>{
    Boolean existsByPlateNumber(String plateNumber);

    @Query("SELECT c FROM CarEntity c WHERE c.status = 'AVAILABLE' AND c.id NOT IN (" +
            "  SELECT b.car.id FROM BookingEntity b WHERE b.status IN ('PENDING', 'CONFIRMED', 'ACTIVE') " +
            "  AND (:pickupDate < b.returnDate AND :returnDate > b.pickupDate)" +
            ")")
    List<CarEntity> findAvailableCars(@Param("pickupDate") LocalDateTime pickupDate,
                                      @Param("returnDate") LocalDateTime returnDate);
}
