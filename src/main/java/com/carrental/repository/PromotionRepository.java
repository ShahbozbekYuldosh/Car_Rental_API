package com.carrental.repository;

import com.carrental.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    List<PromotionEntity> findByActiveTrueAndEndDateAfter(LocalDateTime currentDate);
    Optional<PromotionEntity> findByCode(String code);
}
