package com.carrental.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phoneNumber;

    private String address;

    @NotBlank
    private String driverLicenseNumber;
}
