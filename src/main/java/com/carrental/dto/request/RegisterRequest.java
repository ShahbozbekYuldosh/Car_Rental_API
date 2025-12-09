package com.carrental.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email bo'sh bo'lishi mumkin emas")
    @Email(message = "Email formati noto'g'ri")
    private String email;

    @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
    @Size(min = 6, message = "Parol kamida 6 ta belgidan iborat bo'lishi kerak")
    private String password;

    @NotBlank(message = "Ism bo'sh bo'lishi mumkin emas")
    private String firstName;

    @NotBlank(message = "Familiya bo'sh bo'lishi mumkin emas")
    private String lastName;

    @NotBlank(message = "Telefon bo'sh bo'lishi mumkin emas")
    private String phone;

    private String driverLicense;
}