package com.carrental.dto.response.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopCarResponse {
    private String carName;
    private Long rentalCount;
}