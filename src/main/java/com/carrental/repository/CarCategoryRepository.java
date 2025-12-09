package com.carrental.repository;

import com.carrental.entity.CarCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarCategoryRepository extends JpaRepository<CarCategoryEntity, Long> {

    Boolean existsByName(String name);
    Optional<CarCategoryEntity> findByName(String name);
}
