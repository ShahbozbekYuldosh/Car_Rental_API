package com.carrental.controller;

import com.carrental.dto.request.CarCreateRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.CarResponse;
import com.carrental.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars() {
        List<CarResponse> cars = carService.getAllCars();
        return ResponseEntity.ok(ApiResponse.success(cars));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> getCarById(@PathVariable Long id) {
        CarResponse car = carService.getCarById(id);
        return ResponseEntity.ok(ApiResponse.success(car));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAvailableCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<CarResponse> cars = carService.getAvailableCars(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(cars));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CarResponse>>> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId) {
        List<CarResponse> cars = carService.searchCars(brand, model, minPrice, maxPrice, categoryId, locationId);
        return ResponseEntity.ok(ApiResponse.success(cars));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<CarResponse>> createCar(@Valid @RequestBody CarCreateRequest request) {
        CarResponse car = carService.createCar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mashina muvaffaqiyatli qo'shildi", car));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<CarResponse>> updateCar(
            @PathVariable Long id,
            @Valid @RequestBody CarCreateRequest request) {
        CarResponse car = carService.updateCar(id, request);
        return ResponseEntity.ok(ApiResponse.success("Mashina yangilandi", car));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok(ApiResponse.success("Mashina o'chirildi"));
    }
}