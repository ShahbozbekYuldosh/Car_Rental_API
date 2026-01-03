package com.carrental.enums;

public enum BookingStatus {
    PENDING,      // To'lov kutilmoqda yoki admin tasdig'i
    CONFIRMED,    // Tasdiqlangan va to'langan
    ACTIVE,       // Mashina hozir mijozda
    COMPLETED,    // Mashina muvaffaqiyatli qaytarildi
    CANCELLED,    // Bekor qilingan
    REJECTED      // Admin tomonidan rad etilgan
}