package com.carrental.service;

import com.carrental.dto.request.CarCreateRequest;
import com.carrental.dto.response.CarResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CarService {
    CarResponse createCar(CarCreateRequest request);
    CarResponse updateCar(Long id, CarCreateRequest request);
    void deleteCar(Long id);
    CarResponse getCarById(Long id);
    List<CarResponse> getAllCars();
    List<CarResponse> getAvailableCars(LocalDateTime startDate, LocalDateTime endDate);
    List<CarResponse> searchCars(String brand, String model, BigDecimal minPrice,
                                 BigDecimal maxPrice, Long categoryId, Long locationId);
}

