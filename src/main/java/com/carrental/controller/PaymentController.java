package com.carrental.controller;

import com.carrental.dto.request.PaymentRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    // PaymentService qo'shish kerak

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request) {
        // Payment processing logic
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("To'lov qabul qilindi", null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        // Get payment logic
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByBookingId(@PathVariable Long bookingId) {
        // Get payment by booking logic
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
