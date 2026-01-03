package com.carrental.controller.admin;

import com.carrental.service.AdminService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin paneli", description = "Foydalanuvchi hujjatlarini (pasport/litsenziya) tekshirish va profilni aktivlashtirish operatsiyalari")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Foydalanuvchi profilini tasdiqlash",
            description = "Admin foydalanuvchining yuklagan hujjatlarini tekshirib chiqqach, unga tizimdan to'liq foydalanish huquqini beradi. Status: ACTIVE ga o'zgaradi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foydalanuvchi muvaffaqiyatli faollashtirildi",
                    content = @Content(schema = @Schema(type = "string", example = "Foydalanuvchi muvaffaqiyatli tasdiqlandi!"))),
            @ApiResponse(responseCode = "400", description = "Foydalanuvchi hali barcha OTP (email/sms) verifikatsiyalaridan o'tmagan"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi (Token yo'q yoki yaroqsiz)"),
            @ApiResponse(responseCode = "403", description = "Ruxsat etilmagan (Sizda ADMIN roli yo'q)"),
            @ApiResponse(responseCode = "404", description = "Foydalanuvchi bazadan topilmadi")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{username}/approve")
    public ResponseEntity<String> approveUser(@PathVariable String username) {
        return ResponseEntity.ok(adminService.approveUser(username));
    }

    @Operation(
            summary = "Foydalanuvchi arizasini rad etish",
            description = "Agar hujjatlar talabga javob bermasa (masalan, rasm xira bo'lsa), admin sababini ko'rsatgan holda arizani rad etadi. Status: REJECTED ga o'zgaradi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foydalanuvchi arizasi rad etildi",
                    content = @Content(schema = @Schema(type = "string", example = "Foydalanuvchi rad etildi. Sabab: Hujjat rasmi aniq emas"))),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi"),
            @ApiResponse(responseCode = "403", description = "Ruxsat etilmagan (Faqat ADMINlar uchun)"),
            @ApiResponse(responseCode = "404", description = "Foydalanuvchi topilmadi")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{username}/reject")
    public ResponseEntity<String> rejectUser(
            @PathVariable String username,
            @RequestParam String reason) {
        return ResponseEntity.ok(adminService.rejectUser(username, reason));
    }
}