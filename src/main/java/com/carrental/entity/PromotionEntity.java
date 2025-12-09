package com.carrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Column(nullable = false)
    private BigDecimal discountPercentage;

    private BigDecimal maxDiscountAmount;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private Integer usageLimit;

    private Integer usedCount = 0;

    private Boolean active = true;
}
