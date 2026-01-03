package com.carrental.controller;

import com.carrental.dto.request.CarRequest;
import com.carrental.enums.CarStatus;
import com.carrental.repository.CarRepository;
import com.carrental.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cars")
@RequiredArgsConstructor
@Tag(name = "Admin: Cars", description = "Avtomobillar inventarini boshqarish: rasm yuklash, statusni o'zgartirish va tahrirlash")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class CarController {

    private final CarService carService;
    private final CarRepository carRepository;

    @Operation(
            summary = "Yangi avtomobil qo'shish (Rasmlar bilan)",
            description = "Avtomobil ma'lumotlarini JSON formatida (info) va bir nechta rasmlarni (images) multipart/form-data ko'rinishida yuboring."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mashina va rasmlar muvaffaqiyatli saqlandi"),
            @ApiResponse(responseCode = "400", description = "Ma'lumotlar xato yuborilgan yoki rasm formati noto'g'ri"),
            @ApiResponse(responseCode = "403", description = "Admin huquqi yo'q")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> add(
            @RequestPart("info") @Valid CarRequest request,
            @RequestPart("images") @Parameter(description = "Mashina rasmlari (bir nechta yuklash mumkin)") List<MultipartFile> images) {
        carService.create(request, images);
        return ResponseEntity.ok("Mashina va rasmlar muvaffaqiyatli saqlandi");
    }

    @Operation(
            summary = "Avtomobil ma'lumotlarini yangilash",
            description = "Mavjud avtomobil ma'lumotlarini va rasmlarini yangilash. Rasmlar majburiy emas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mashina ma'lumotlari yangilandi"),
            @ApiResponse(responseCode = "404", description = "Mashina topilmadi")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> update(
            @Parameter(description = "Avtomobil ID raqami", example = "1") @PathVariable Long id,
            @RequestPart("info") @Valid CarRequest request,
            @RequestPart(value = "images", required = false) @Parameter(description = "Yangi rasmlar (ixtiyoriy)") List<MultipartFile> images) {
        carService.update(id, request, images);
        return ResponseEntity.ok("Mashina ma'lumotlari yangilandi");
    }

    @Operation(summary = "Avtomobilni o'chirish", description = "Mashinani va unga tegishli barcha rasmlarni serverdan o'chirib tashlaydi.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli o'chirildi"),
            @ApiResponse(responseCode = "404", description = "Mashina topilmadi")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Avtomobil ID raqami", example = "1") @PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.ok("Mashina va uning barcha rasmlari o'chirildi");
    }

    @Operation(
            summary = "Avtomobil statusini o'zgartirish",
            description = "Mashinani ijaraga tayyor (AVAILABLE), ijarada (RENTED) yoki ta'mirda (MAINTENANCE) holatlariga o'tkazish."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status muvaffaqiyatli yangilandi"),
            @ApiResponse(responseCode = "400", description = "Noto'g'ri status kiritilgan")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @Parameter(description = "Avtomobil ID raqami", example = "1") @PathVariable Long id,
            @Parameter(description = "Yangi status", schema = @Schema(implementation = CarStatus.class))
            @RequestParam CarStatus status) {
        carRepository.updateCarStatusNative(id, status.name());
        return ResponseEntity.ok("Status o'zgardi: " + status);
    }
}