package com.carrental.controller.admin;

import com.carrental.dto.response.admin.AdminDashboardResponse;
import com.carrental.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Admin paneli uchun asosiy statistika va ko'rsatkichlarni boshqarish")
@SecurityRequirement(name = "bearerAuth")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "Admin paneli statistikalarini olish",
            description = "Tizimdagi jami foydalanuvchilar, avtomobillar, faol bandlovlar (bookings) va umumiy moliyaviy statistikani qaytaradi."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statistika muvaffaqiyatli yuklandi",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminDashboardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Avtorizatsiya xatosi (Token xato yoki yo'q)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Ruxsat etilmagan (Faqat ADMIN huquqiga ega foydalanuvchilar uchun)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Serverda ichki xatolik yuz berdi",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardResponse> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
}