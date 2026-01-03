package com.carrental.repository;

import com.carrental.entity.CarCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarCompanyRepository extends JpaRepository<CarCompany, Long> {

    // Kompaniyalarni paginatsiya bilan olish
    @Query(value = "SELECT * FROM car_companies " +
            "WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "ORDER BY id DESC LIMIT :size OFFSET :offset", nativeQuery = true)
    List<CarCompany> findAllPaginatedNative(@Param("name") String name,
                                            @Param("size") int size,
                                            @Param("offset") int offset);

    // Umumiy sonini hisoblash (Paginatsiya uchun kerak)
    @Query(value = "SELECT COUNT(*) FROM car_companies " +
            "WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    long countByNameNative(@Param("name") String name);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM car_companies WHERE name = :name)", nativeQuery = true)
    boolean existsByNameNative(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE car_companies SET name = :name, country = :country, website = :website, updated_at = NOW() WHERE id = :id", nativeQuery = true)
    void updateCompanyNative(@Param("id") Long id, @Param("name") String name,
                             @Param("country") String country, @Param("website") String website);
}