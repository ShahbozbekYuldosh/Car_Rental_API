package com.carrental.service;
import com.carrental.dto.request.CarRequest;
import com.carrental.dto.response.CarResponse;
import com.carrental.dto.response.PageResponse;
import com.carrental.entity.Car;
import com.carrental.entity.CarImage;
import com.carrental.entity.CarVariant;
import com.carrental.repository.CarRepository;
import com.carrental.repository.CarVariantRepository;
import com.carrental.repository.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final CarVariantRepository variantRepository;
    private final FileService fileService;

    public CarResponse getById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mashina topilmadi"));
        return mapToResponse(car);
    }

    @Transactional
    public void create(CarRequest request, List<MultipartFile> images) {
        if (carRepository.countByRegistrationNumberNative(request.getRegistrationNumber()) > 0) {
            throw new RuntimeException("Bu raqamli mashina allaqachon mavjud!");
        }

        if (images == null || images.isEmpty() || images.size() > 6) {
            throw new RuntimeException("1 tadan 6 tagacha rasm yuklash shart!");
        }

        CarVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant topilmadi"));

        Car car = new Car();
        updateCarFields(car, request, variant);

        // Rasmlarni saqlash
        for (MultipartFile img : images) {
            String url = fileService.saveImage(img, "cars");
            car.getImages().add(new CarImage(url, car));
        }

        carRepository.save(car);
    }

    @Transactional
    public void update(Long id, CarRequest request, List<MultipartFile> newImages) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mashina topilmadi"));

        CarVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant topilmadi"));

        updateCarFields(car, request, variant);

        if (newImages != null && !newImages.isEmpty()) {
            for (CarImage image : car.getImages()) {
                fileService.deleteFile(image.getImageUrl());
            }
            car.getImages().clear();
            carRepository.saveAndFlush(car);

            for (MultipartFile img : newImages) {
                String url = fileService.saveImage(img, "cars");
                car.getImages().add(new CarImage(url, car));
            }
        }
        carRepository.save(car);
    }

    /**
     * DINAMIK QIDIRUV VA SAHIFALASH
     */
    @Transactional(readOnly = true)
    public PageResponse<CarResponse> searchCars(String company, String model, String color,
                                                Double min, Double max, Boolean hasAC,
                                                int page, int size) {

        Specification<Car> spec = Specification.where(CarSpecification.isAvailable())
                .and(CarSpecification.hasColor(color))
                .and(CarSpecification.hasPriceBetween(min, max))
                .and(CarSpecification.hasVariantName(model))
                .and(CarSpecification.hasCompanyName(company))
                .and(CarSpecification.hasAirConditioning(hasAC));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Car> carPage = carRepository.findAll(spec, pageable);

        List<CarResponse> content = carPage.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponse.<CarResponse>builder()
                .content(content)
                .totalElements(carPage.getTotalElements())
                .totalPages(carPage.getTotalPages())
                .number(carPage.getNumber())
                .size(carPage.getSize())
                .build();
    }

    private void updateCarFields(Car car, CarRequest request, CarVariant variant) {
        car.setRegistrationNumber(request.getRegistrationNumber());
        car.setColor(request.getColor());
        car.setHourlyRate(request.getHourlyRate());
        car.setDailyRate(request.getDailyRate());
        car.setDoors(request.getDoors());
        car.setPassengerCapacity(request.getPassengerCapacity());
        car.setLuggageCapacity(request.getLuggageCapacity());
        car.setHasAirConditioning(request.getHasAirConditioning());
        car.setAdditionalFeatures(request.getAdditionalFeatures());
        car.setVariant(variant);
    }

    @Transactional
    public void delete(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mashina topilmadi"));

        for (CarImage image : car.getImages()) {
            fileService.deleteFile(image.getImageUrl());
        }

        carRepository.delete(car);
    }

    private CarResponse mapToResponse(Car car) {
        String model = car.getVariant() != null ? car.getVariant().getModel() : "Noma'lum";
        String variant = car.getVariant() != null ? car.getVariant().getVariantName() : "";
        String company = (car.getVariant() != null && car.getVariant().getCompany() != null)
                ? car.getVariant().getCompany().getName() : "Noma'lum";

        return CarResponse.builder()
                .id(car.getId())
                .registrationNumber(car.getRegistrationNumber())
                .color(car.getColor())
                .hourlyRate(car.getHourlyRate())
                .dailyRate(car.getDailyRate())
                .status(car.getStatus().name())
                .doors(car.getDoors())
                .passengerCapacity(car.getPassengerCapacity())
                .luggageCapacity(car.getLuggageCapacity())
                .hasAirConditioning(car.getHasAirConditioning())
                .additionalFeatures(car.getAdditionalFeatures())
                .modelName(model + " " + variant)
                .companyName(company)
                .imageUrls(car.getImages().stream()
                        .map(CarImage::getImageUrl)
                        .toList())
                .build();
    }
}