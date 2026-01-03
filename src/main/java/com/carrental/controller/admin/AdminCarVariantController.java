package com.carrental.controller.admin;

import com.carrental.dto.request.CarVariantRequest;
import com.carrental.dto.response.CarVariantResponse;
import com.carrental.dto.response.PageResponse;
import com.carrental.service.CarVariantService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/variants")
@RequiredArgsConstructor
@Tag(name = "Admin: Car Variants", description = "Avtomobil variantlarini (modellarini) boshqarish: yaratish, tahrirlash va o'chirish")
@SecurityRequirement(name = "bearerAuth")
public class AdminCarVariantController {

    private final CarVariantService carVariantService;

    @Operation(
            summary = "Yangi avtomobil varianti qo'shish",
            description = "Kompaniya ID raqami va model ma'lumotlari orqali yangi variant yaratadi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = CarVariantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Noto'g'ri ma'lumot yuborilgan"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi"),
            @ApiResponse(responseCode = "403", description = "Ruxsat etilmagan (ADMIN emas)")
    })
    @PostMapping
    public ResponseEntity<CarVariantResponse> add(@Valid @RequestBody CarVariantRequest request) {
        return ResponseEntity.ok(carVariantService.create(request));
    }

    @Operation(
            summary = "Variantlar ro'yxatini olish (Pagination)",
            description = "Barcha variantlarni sahifalangan holda qaytaradi. Kompaniya bo'yicha filtrlash va qidiruv imkoniyati mavjud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ro'yxat muvaffaqiyatli yuklandi"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi")
    })
    @GetMapping
    public ResponseEntity<PageResponse<CarVariantResponse>> list(
            @Parameter(description = "Kompaniya ID raqami bo'yicha filtrlash") @RequestParam(required = false) Long companyId,
            @Parameter(description = "Model nomi bo'yicha qidiruv") @RequestParam(defaultValue = "") String search,
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(carVariantService.getAll(companyId, search, page, size));
    }

    @Operation(
            summary = "Variant ma'lumotlarini tahrirlash",
            description = "Mavjud variant ma'lumotlarini ID raqami orqali yangilaydi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli yangilandi"),
            @ApiResponse(responseCode = "404", description = "Variant topilmadi"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarVariantResponse> update(
            @Parameter(description = "Variant ID raqami", example = "1") @PathVariable Long id,
            @Valid @RequestBody CarVariantRequest request) {
        return ResponseEntity.ok(carVariantService.update(id, request));
    }

    @Operation(
            summary = "Variantni o'chirish",
            description = "Variantni tizimdan butunlay o'chirib tashlaydi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant muvaffaqiyatli o'chirildi"),
            @ApiResponse(responseCode = "404", description = "Variant topilmadi"),
            @ApiResponse(responseCode = "409", description = "O'chirish imkonsiz (ushbu variantga bog'langan avtomobillar mavjud)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Variant ID raqami", example = "1") @PathVariable Long id) {
        carVariantService.delete(id);
        return ResponseEntity.ok("Variant muvaffaqiyatli o'chirildi");
    }
}