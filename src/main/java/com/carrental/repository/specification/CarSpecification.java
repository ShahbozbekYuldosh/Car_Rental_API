package com.carrental.repository.specification;

import com.carrental.entity.Car;
import com.carrental.entity.CarCompany;
import com.carrental.entity.CarVariant;
import com.carrental.enums.CarStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {

    // Rang bo'yicha filtr
    public static Specification<Car> hasColor(String color) {
        return (root, query, cb) -> color == null ? null : cb.equal(root.get("color"), color);
    }

    // Maksimal kunlik narx bo'yicha filtr
    public static Specification<Car> hasMaxDailyRate(Double maxRate) {
        return (root, query, cb) -> maxRate == null ? null : cb.lessThanOrEqualTo(root.get("dailyRate"), maxRate);
    }

    // Konditsioner borligi bo'yicha
    public static Specification<Car> hasAirConditioning(Boolean hasAC) {
        return (root, query, cb) -> hasAC == null ? null : cb.equal(root.get("hasAirConditioning"), hasAC);
    }

    // Faqat bo'sh turgan mashinalarni ko'rsatish
    public static Specification<Car> isAvailable() {
        return (root, query, cb) -> cb.equal(root.get("status"), CarStatus.AVAILABLE);
    }

    // 2. Narx oralig'i (Between)
    public static Specification<Car> hasPriceBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("dailyRate"), min, max);
            if (min != null) return cb.greaterThanOrEqualTo(root.get("dailyRate"), min);
            return cb.lessThanOrEqualTo(root.get("dailyRate"), max);
        };
    }

    // 3. JOIN - Variant nomi bo'yicha (masalan: Malibu)
    public static Specification<Car> hasVariantName(String variantName) {
        return (root, query, cb) -> {
            if (variantName == null || variantName.isEmpty()) return null;
            Join<Car, CarVariant> variantJoin = root.join("variant");
            return cb.like(cb.lower(variantJoin.get("model")), "%" + variantName.toLowerCase() + "%");
        };
    }

    // 4. DEEP JOIN - Kompaniya nomi bo'yicha (masalan: Chevrolet)
    public static Specification<Car> hasCompanyName(String companyName) {
        return (root, query, cb) -> {
            if (companyName == null || companyName.isEmpty()) return null;
            // Car -> CarVariant -> CarCompany
            Join<Car, CarVariant> variantJoin = root.join("variant");
            Join<CarVariant, CarCompany> companyJoin = variantJoin.join("company");
            return cb.equal(cb.lower(companyJoin.get("name")), companyName.toLowerCase());
        };
    }
}