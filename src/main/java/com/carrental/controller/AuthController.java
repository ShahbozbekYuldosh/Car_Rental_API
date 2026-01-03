package com.carrental.controller;

import com.carrental.config.security.SpringSecurityUtil;
import com.carrental.dto.request.*;
import com.carrental.dto.response.JwtResponse;
import com.carrental.dto.response.UserResponse;
import com.carrental.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autentifikatsiya", description = "Tizimga kirish, ro'yxatdan o'tish, OTP tasdiqlash va tokenlarni boshqarish")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Yangi foydalanuvchini ro'yxatdan o'tkazish",
            description = "Foydalanuvchi ma'lumotlari bilan birga pasport va litsenziya rasmlarini yuklaydi. Status: REGISTRATION_PROGRESS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Muvaffaqiyatli ro'yxatdan o'tildi",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Username yoki email allaqachon mavjud yoki ma'lumotlar xato yuborilgan"),
            @ApiResponse(responseCode = "500", description = "Fayl yuklashda yoki serverda xatolik")
    })
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> register(
            @RequestPart("info") String infoJson,
            @RequestPart("passport") MultipartFile passport,
            @RequestPart("license") MultipartFile license
    ) throws Exception {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        RegisterRequest request = mapper.readValue(infoJson, RegisterRequest.class);

        return new ResponseEntity<>(authService.register(request, passport, license), HttpStatus.CREATED);
    }

    @Operation(summary = "Email manzilini tasdiqlash (OTP)",
            description = "Emailga yuborilgan 6 xonali kod orqali foydalanuvchini tasdiqlash.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email tasdiqlandi"),
            @ApiResponse(responseCode = "400", description = "Kod xato yoki foydalanuvchi topilmadi")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerifyRequest request) {
        authService.verifyEmail(request.getEmail(), request.getCode());
        return ResponseEntity.ok("Email muvaffaqiyatli tasdiqlandi!");
    }

    @Operation(summary = "Telefon raqamini tasdiqlash (OTP)",
            description = "Telefonga SMS orqali yuborilgan 6 xonali kod orqali foydalanuvchini tasdiqlash.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefon raqami tasdiqlandi"),
            @ApiResponse(responseCode = "400", description = "Kod xato yoki foydalanuvchi topilmadi")
    })
    @PostMapping("/verify-phone")
    public ResponseEntity<?> verifyPhone(@RequestBody PhoneVerifyRequest request) {
        authService.verifyPhone(request.getPhone(), request.getCode());
        return ResponseEntity.ok("Telefon raqami muvaffaqiyatli tasdiqlandi!");
    }

    @Operation(summary = "Tizimga kirish (Login)",
            description = "Faqat statusi 'ACTIVE' bo'lgan foydalanuvchilar kirishi mumkin. JWT Access va Refresh token qaytaradi.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli kirildi",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Login yoki parol xato"),
            @ApiResponse(responseCode = "403", description = "Profil hali admin tomonidan tasdiqlanmagan")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Tokenni yangilash",
            description = "Eski Refresh token orqali yangi Access token olish.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token muvaffaqiyatli yangilandi"),
            @ApiResponse(responseCode = "401", description = "Refresh token muddati o'tgan yoki xato")
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    @Operation(summary = "Tizimdan chiqish",
            description = "Faqat login qilgan foydalanuvchilar uchun. Tokenlarni bekor qiladi va statusni nofaol qiladi.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli chiqildi"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Long currentUserId = SpringSecurityUtil.getCurrentUserId();
        authService.logout(currentUserId);
        return ResponseEntity.ok("Muvaffaqiyatli chiqildi.");
    }
}