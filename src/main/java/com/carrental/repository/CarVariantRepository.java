package com.carrental.repository;

import com.carrental.entity.CarVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarVariantRepository extends JpaRepository<CarVariant, Long> {

    @Query(value = "SELECT cv.* FROM car_variants cv " +
            "WHERE (:companyId IS NULL OR cv.company_id = :companyId) " +
            "AND (LOWER(cv.model) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(cv.variant_name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY cv.id DESC LIMIT :size OFFSET :offset", nativeQuery = true)
    List<CarVariant> findAllPaginatedNative(@Param("companyId") Long companyId,
                                            @Param("search") String search,
                                            @Param("size") int size,
                                            @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM car_variants " +
            "WHERE (:companyId IS NULL OR company_id = :companyId) " +
            "AND (LOWER(model) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(variant_name) LIKE LOWER(CONCAT('%', :search, '%')))", nativeQuery = true)
    long countByFiltersNative(@Param("companyId") Long companyId, @Param("search") String search);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM car_variants WHERE id = :id", nativeQuery = true)
    void deleteVariantNative(@Param("id") Long id);
}