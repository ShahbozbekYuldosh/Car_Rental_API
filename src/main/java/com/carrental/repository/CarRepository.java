package com.carrental.repository;

import com.carrental.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends  JpaRepository<CarEntity, Long>{
    Boolean existsByPlateNumber(String plateNumber);

    @Query("SELECT c FROM CarEntity c WHERE c.status = 'AVAILABLE' AND c.id NOT IN (" +
            "  SELECT b.car.id FROM BookingEntity b WHERE b.status IN ('PENDING', 'CONFIRMED', 'ACTIVE') " +
            "  AND (:pickupDate < b.returnDate AND :returnDate > b.pickupDate)" +
            ")")
    List<CarEntity> findAvailableCars(@Param("pickupDate") LocalDateTime pickupDate,
                                      @Param("returnDate") LocalDateTime returnDate);

    @Query("SELECT c FROM CarEntity c LEFT JOIN FETCH c.images WHERE c.id = :id")
    Optional<CarEntity> findByIdWithImages(@Param("id") Long id);

    @Query("SELECT c FROM CarEntity c WHERE " +
            // Brand bo'yicha qidiruv
            "(:brand IS NULL OR LOWER(c.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            // Model bo'yicha qidiruv
            "(:model IS NULL OR LOWER(c.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
            // Minimal narx
            "(:minPrice IS NULL OR c.dailyRate >= :minPrice) AND " +
            // Maksimal narx
            "(:maxPrice IS NULL OR c.dailyRate <= :maxPrice) AND " +
            // Kategoriya ID bo'yicha
            "(:categoryId IS NULL OR c.category.id = :categoryId) AND " +
            // Lokatsiya ID bo'yicha
            "(:locationId IS NULL OR c.location.id = :locationId) AND " +
            // Faqat SOTIB OLISHGA tayyor yoki IJARADA BO'LMAGAN mashinalarni ko'rsatish
            "(c.status = 'AVAILABLE' OR c.status = 'MAINTENANCE')" // STATUS filtri
    )
    List<CarEntity> searchCars(
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("categoryId") Long categoryId,
            @Param("locationId") Long locationId
    );
}
