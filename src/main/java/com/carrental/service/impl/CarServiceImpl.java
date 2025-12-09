package com.carrental.service.impl;

import com.carrental.dto.request.CarCreateRequest;
import com.carrental.dto.response.CarResponse;
import com.carrental.entity.CarCategoryEntity;
import com.carrental.entity.CarEntity;
import com.carrental.entity.LocationEntity;
import com.carrental.enums.CarStatus;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.repository.CarCategoryRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.LocationRepository;
import com.carrental.repository.ReviewRepository;
import com.carrental.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarCategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public CarResponse createCar(CarCreateRequest request) {
        CarCategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategoriya topilmadi"));

        LocationEntity location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Joylashuv topilmadi"));

        CarEntity car = CarEntity.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .plateNumber(request.getPlateNumber())
                .color(request.getColor())
                .transmission(request.getTransmission())
                .fuelType(request.getFuelType())
                .seats(request.getSeats())
                .dailyRate(request.getDailyRate())
                .mileage(request.getMileage())
                .status(CarStatus.AVAILABLE)
                .category(category)
                .location(location)
                .airConditioning(request.getAirConditioning())
                .gps(request.getGps())
                .bluetooth(request.getBluetooth())
                .childSeat(request.getChildSeat())
                .description(request.getDescription())
                .build();

        car = carRepository.save(car);
        return mapToCarResponse(car);
    }

    @Override
    @Transactional
    public CarResponse updateCar(Long id, CarCreateRequest request) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mashina topilmadi"));

        CarCategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategoriya topilmadi"));

        LocationEntity location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Joylashuv topilmadi"));

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPlateNumber(request.getPlateNumber());
        car.setColor(request.getColor());
        car.setTransmission(request.getTransmission());
        car.setFuelType(request.getFuelType());
        car.setSeats(request.getSeats());
        car.setDailyRate(request.getDailyRate());
        car.setMileage(request.getMileage());
        car.setCategory(category);
        car.setLocation(location);
        car.setAirConditioning(request.getAirConditioning());
        car.setGps(request.getGps());
        car.setBluetooth(request.getBluetooth());
        car.setChildSeat(request.getChildSeat());
        car.setDescription(request.getDescription());

        car = carRepository.save(car);
        return mapToCarResponse(car);
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mashina topilmadi"));
        carRepository.delete(car);
    }

    @Override
    public CarResponse getCarById(Long id) {
        CarEntity car = carRepository.findByIdWithImages(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mashina topilmadi"));
        return mapToCarResponse(car);
    }

    @Override
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarResponse> getAvailableCars(LocalDateTime startDate, LocalDateTime endDate) {
        return carRepository.findAvailableCars(startDate, endDate).stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarResponse> searchCars(String brand, String model, BigDecimal minPrice,
                                        BigDecimal maxPrice, Long categoryId, Long locationId) {
        return carRepository.searchCars(brand, model, minPrice, maxPrice, categoryId, locationId)
                .stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());
    }

    private CarResponse mapToCarResponse(CarEntity car) {
        Optional<Double> optionalAverageRating = reviewRepository.getAverageRatingByCarId(car.getId());
        Double averageRating = optionalAverageRating.orElse(0.0);
        Long reviewCount = reviewRepository.countApprovedReviewsByCarId(car.getId());


        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .year(car.getYear())
                .plateNumber(car.getPlateNumber())
                .color(car.getColor())
                .transmission(car.getTransmission())
                .fuelType(car.getFuelType())
                .seats(car.getSeats())
                .dailyRate(car.getDailyRate())
                .mileage(car.getMileage())
                .status(car.getStatus())
                .categoryName(car.getCategory().getName())
                .locationName(car.getLocation().getName())
                .airConditioning(car.getAirConditioning())
                .gps(car.getGps())
                .bluetooth(car.getBluetooth())
                .childSeat(car.getChildSeat())
                .description(car.getDescription())
                .imageUrls(car.getImages().stream()
                        .map(img -> img.getImageUrl())
                        .collect(Collectors.toList()))
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .build();
    }
}
