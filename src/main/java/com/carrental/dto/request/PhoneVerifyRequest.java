package com.carrental.dto.request;

import lombok.Data;

@Data
public class PhoneVerifyRequest {
    private String phone;
    private String code;
}