package com.carrental.controller.admin;

import com.carrental.dto.request.CarCompanyRequest;
import com.carrental.dto.response.CarCompanyResponse;
import com.carrental.dto.response.PageResponse;
import com.carrental.entity.CarCompany;
import com.carrental.service.CarCompanyService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
@Tag(name = "Admin: Car Companies", description = "Avtomobil ishlab chiqaruvchi kompaniyalarni (brendlarni) boshqarish operatsiyalari")
@SecurityRequirement(name = "bearerAuth")
public class AdminCarCompanyController {

    private final CarCompanyService carCompanyService;

    @Operation(
            summary = "Yangi avtomobil kompaniyasini qo'shish",
            description = "Tizimga yangi brend (masalan: BMW, Mercedes) qo'shadi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kompaniya muvaffaqiyatli yaratildi",
                    content = @Content(schema = @Schema(implementation = CarCompany.class))),
            @ApiResponse(responseCode = "400", description = "Yuborilgan ma'lumotlarda xatolik bor"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi"),
            @ApiResponse(responseCode = "403", description = "Ruxsat etilmagan (ADMIN roli talab qilinadi)")
    })
    @PostMapping
    public ResponseEntity<CarCompany> add(@RequestBody CarCompanyRequest request) {
        return ResponseEntity.ok(carCompanyService.create(request));
    }

    @Operation(
            summary = "Kompaniyalar ro'yxatini olish (Pagination)",
            description = "Barcha kompaniyalarni sahifalangan holda qaytaradi. Nomi bo'yicha qidirish imkoniyati mavjud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ro'yxat muvaffaqiyatli yuklandi",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi")
    })
    @GetMapping
    public ResponseEntity<PageResponse<CarCompanyResponse>> list(
            @Parameter(description = "Kompaniya nomi bo'yicha qidiruv", example = "Chevrolet")
            @RequestParam(defaultValue = "") String name,
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(carCompanyService.getAll(name, page, size));
    }

    @Operation(
            summary = "Kompaniya ma'lumotlarini yangilash",
            description = "Mavjud kompaniya nomi yoki boshqa ma'lumotlarini ID orqali tahrirlaydi."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli yangilandi"),
            @ApiResponse(responseCode = "404", description = "Kompaniya topilmadi"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiya xatosi")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "Kompaniya ID raqami", example = "1") @PathVariable Long id,
            @RequestBody CarCompanyRequest request) {
        carCompanyService.update(id, request);
        return ResponseEntity.ok("Muvaffaqiyatli yangilandi");
    }

    @Operation(
            summary = "Kompaniyani o'chirish",
            description = "Kompaniyani tizimdan butunlay o'chiradi. Diqqat: Agar ushbu kompaniyaga bog'langan modellar bo'lsa, xatolik berishi mumkin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli o'chirildi"),
            @ApiResponse(responseCode = "404", description = "Kompaniya topilmadi"),
            @ApiResponse(responseCode = "409", description = "O'chirish imkonsiz (bog'langan ma'lumotlar mavjud)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "Kompaniya ID raqami", example = "1") @PathVariable Long id) {
        carCompanyService.delete(id);
        return ResponseEntity.ok("Muvaffaqiyatli o'chirildi");
    }
}