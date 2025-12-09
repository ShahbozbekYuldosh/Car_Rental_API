package com.carrental.controller;

import com.carrental.dto.response.ApiResponse;
import com.carrental.enums.BookingStatus;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.PaymentRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // User statistics
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);

        // Booking statistics
        long totalBookings = bookingRepository.count();
        long activeBookings = bookingRepository.countByStatus(BookingStatus.ACTIVE);
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);

        stats.put("totalBookings", totalBookings);
        stats.put("activeBookings", activeBookings);
        stats.put("pendingBookings", pendingBookings);

        // Revenue statistics (last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        BigDecimal monthlyRevenue = paymentRepository.calculateTotalRevenue(
                thirtyDaysAgo, LocalDateTime.now());

        stats.put("monthlyRevenue", monthlyRevenue);

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}