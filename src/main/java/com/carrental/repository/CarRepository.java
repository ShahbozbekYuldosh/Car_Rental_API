package com.carrental.repository;

import com.carrental.entity.Car;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    @Query(value = "SELECT COUNT(*) FROM cars WHERE registration_number = :regNum", nativeQuery = true)
    long countByRegistrationNumberNative(@Param("regNum") String regNum);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cars SET status = :status, updated_at = NOW() WHERE id = :id", nativeQuery = true)
    void updateCarStatusNative(@Param("id") Long id, @Param("status") String status);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM cars WHERE id = :id)", nativeQuery = true)
    boolean existsByIdNative(@Param("id") Long id);
}