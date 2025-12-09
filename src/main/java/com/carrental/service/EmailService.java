package com.carrental.service;

public interface EmailService {
    void sendVerificationEmail(String to, String token);
    void sendPasswordResetEmail(String to, String token);
    void sendBookingConfirmation(String to, Long bookingId);
}
