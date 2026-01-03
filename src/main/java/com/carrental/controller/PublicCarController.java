package com.carrental.controller;

import com.carrental.dto.response.CarResponse;
import com.carrental.dto.response.PageResponse;
import com.carrental.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars") // Hamma uchun ochiq yo'l
@RequiredArgsConstructor
@Tag(name = "Public: Cars", description = "Mashinalarni qidirish va ko'rish uchun hamma ishlata oladigan API")
public class PublicCarController {

    private final CarService carService;

    @Operation(
            summary = "Mashinalarni dinamik qidirish (Filter & Pagination)",
            description = "Kompaniya, model, rang, narx va konditsioner bo'yicha qidirish. Parametrlar ixtiyoriy."
    )
    @GetMapping("/search")
    public ResponseEntity<PageResponse<CarResponse>> search(
            @Parameter(description = "Kompaniya nomi (masalan: Chevrolet)") @RequestParam(required = false) String company,
            @Parameter(description = "Model nomi (masalan: Malibu)") @RequestParam(required = false) String model,
            @Parameter(description = "Rangi") @RequestParam(required = false) String color,
            @Parameter(description = "Minimal kunlik narx") @RequestParam(required = false) Double min,
            @Parameter(description = "Maksimal kunlik narx") @RequestParam(required = false) Double max,
            @Parameter(description = "Konditsioner bormi?") @RequestParam(required = false) Boolean hasAC,
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(carService.searchCars(company, model, color, min, max, hasAC, page, size));
    }

    @Operation(summary = "Bitta mashina haqida to'liq ma'lumot olish")
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getById(id));
    }
}