package com.carrental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long bookingId;
    private String userName;
    private Long carId;
    private String carBrand;
    private String carModel;
    private Integer rating;
    private String comment;
    private Boolean approved;
    private LocalDateTime createdAt;
}