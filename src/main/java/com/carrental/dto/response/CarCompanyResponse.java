package com.carrental.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarCompanyResponse {
    private Long id;
    private String name;
    private String country;
    private String website;
    private LocalDateTime createdAt;
}