package com.carrental.dto.response.admin;
import lombok.Builder;
import lombok.Data;
import java.util.Map;
import java.util.List;

@Data
@Builder
public class AdminDashboardResponse {
    private Double totalRevenue;
    private Long totalBookings;
    private Map<String, Long> bookingsByStatus;
    private List<TopCarResponse> topCars;
    private List<RecentPaymentResponse> recentPayments;
}