package com.carrental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarCompanyRequest {

    @NotBlank(message = "Kompaniya nomi bo'sh bo'lmasligi kerak")
    @Size(min = 2, max = 100, message = "Kompaniya nomi 2 dan 100 tagacha belgidan iborat bo'lishi kerak")
    private String name;

    @NotBlank(message = "Mamlakat nomi bo'sh bo'lmasligi kerak")
    private String country;

    @Pattern(regexp = "^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$",
            message = "Noto'g'ri veb-sayt formati")
    private String website;
}