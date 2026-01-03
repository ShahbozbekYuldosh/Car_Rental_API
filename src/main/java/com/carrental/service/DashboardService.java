package com.carrental.service;

import com.carrental.dto.response.admin.AdminDashboardResponse;
import com.carrental.dto.response.admin.TopCarResponse;
import com.carrental.repository.AdminRepository;
import com.carrental.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AdminRepository adminRepository;
    private final PaymentRepository paymentRepository;

    public AdminDashboardResponse getDashboardStats() {
        // 1. Umumiy daromad
        Double revenue = adminRepository.getTotalRevenue();

        // 2. Statuslar bo'yicha bronlar
        Map<String, Long> statusStats = new HashMap<>();
        adminRepository.getBookingStatsNative().forEach(row ->
                statusStats.put((String) row[0], ((Number) row[1]).longValue())
        );

        // 3. Top 5 mashinalar
        List<TopCarResponse> topCars = adminRepository.getTop5CarsNative().stream()
                .map(row -> TopCarResponse.builder()
                        .carName((String) row[0])
                        .rentalCount(((Number) row[1]).longValue())
                        .build())
                .toList();

        return AdminDashboardResponse.builder()
                .totalRevenue(revenue != null ? revenue : 0.0)
                .totalBookings(statusStats.values().stream().mapToLong(Long::longValue).sum())
                .bookingsByStatus(statusStats)
                .topCars(topCars)
                .build();
    }
}