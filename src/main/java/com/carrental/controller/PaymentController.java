package com.carrental.controller;

import com.carrental.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/payments")
@RequiredArgsConstructor
@Tag(name = "User: Payments", description = "Mijozlar tomonidan bandlovlar uchun to'lovlarni amalga oshirish")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Bandlov uchun to'lov qilish",
            description = "Foydalanuvchi o'zi yaratgan bandlov (booking) uchun tashqi to'lov tizimi (Payme, Click va h.k.) orqali amalga oshirgan tranzaksiyasini tasdiqlaydi."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "To'lov muvaffaqiyatli qabul qilindi va bron holati 'CONFIRMED'ga o'zgartirildi",
                    content = @Content(schema = @Schema(type = "string", example = "To'lov muvaffaqiyatli amalga oshirildi. Bron tasdiqlandi."))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Xato ma'lumotlar yuborilgan (masalan, bron allaqachon to'langan yoki bekor qilingan)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Avtorizatsiya xatosi (Token xato)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Bunday ID dagi bandlov topilmadi",
                    content = @Content
            )
    })
    @PostMapping("/pay/{bookingId}")
    public ResponseEntity<String> pay(
            @Parameter(description = "To'lanishi kerak bo'lgan bron (booking) ID raqami", example = "105", required = true)
            @PathVariable Long bookingId,

            @Parameter(description = "Tashqi to'lov tizimidan olingan unikal tranzaksiya ID si", example = "TXN_9988776655", required = true)
            @RequestParam String transactionId,

            @Parameter(description = "To'lov usuli", example = "CLICK", schema = @Schema(allowableValues = {"CLICK", "PAYME", "CASH", "CARD"}))
            @RequestParam String method) {

        paymentService.processPayment(bookingId, transactionId, method);
        return ResponseEntity.ok("To'lov muvaffaqiyatli amalga oshirildi. Bron tasdiqlandi.");
    }
}