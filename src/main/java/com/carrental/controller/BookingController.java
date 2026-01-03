package com.carrental.controller;

import com.carrental.config.security.SpringSecurityUtil;
import com.carrental.dto.request.BookingRequest;
import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.Booking;
import com.carrental.entity.User;
import com.carrental.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
@Tag(name = "User: Bookings", description = "Foydalanuvchilar uchun avtomobil band qilish va boshqarish endpointlari")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;

    @Operation(
            summary = "Yangi bandlov (bron) yaratish",
            description = "Tanlangan avtomobilni ma'lum vaqt oralig'iga band qiladi. Status: PENDING"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bron muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Noto'g'ri vaqt oralig'i yoki mashina band"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi"),
            @ApiResponse(responseCode = "403", description = "Profil faol emas (ACTIVE emas)")
    })
    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request) {
        User currentUser = SpringSecurityUtil.getCurrentEntity();
        Booking booking = bookingService.createBooking(request, currentUser);
        return new ResponseEntity<>(BookingResponse.fromEntity(booking), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Foydalanuvchining barcha bronlari ro'yxati",
            description = "Tizimga kirgan foydalanuvchining barcha o'tgan va joriy bandlovlarini qaytaradi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ro'yxat muvaffaqiyatli yuklandi",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi")
    })
    @GetMapping("/my-list")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        List<BookingResponse> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @Operation(
            summary = "Bandlovni bekor qilish",
            description = "Hali boshlanmagan (PENDING/CONFIRMED) bandlovni sababini ko'rsatgan holda bekor qiladi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bron bekor qilindi"),
            @ApiResponse(responseCode = "400", description = "Bronni bekor qilib bo'lmaydigan holatda"),
            @ApiResponse(responseCode = "403", description = "Ushbu bron sizga tegishli emas"),
            @ApiResponse(responseCode = "404", description = "Bron topilmadi")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelBooking(
            @Parameter(description = "Bron ID raqami", example = "101") @PathVariable Long id,
            @Parameter(description = "Bekor qilish sababi", example = "Rejalarim o'zgardi") @RequestParam String reason) {
        Long userId = SpringSecurityUtil.getCurrentUserId();
        bookingService.cancelBooking(id, userId, reason);
        return ResponseEntity.ok("Bron bekor qilindi.");
    }
}