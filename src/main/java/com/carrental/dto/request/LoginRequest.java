package com.carrental.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email bo'sh bo'lishi mumkin emas")
    @Email(message = "Email formati noto'g'ri")
    private String email;

    @NotBlank(message = "Parol bo'sh bo'lishi mumkin emas")
    private String password;
}
