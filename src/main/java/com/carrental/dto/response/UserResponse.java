package com.carrental.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String driverLicense;
    private LocalDate licenseExpiryDate;
    private String address;
    private String city;
    private String country;
    private Boolean verified;
    private Boolean active;
    private Set<String> roles;
}
