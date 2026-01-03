package com.carrental.service;

import com.carrental.dto.request.CarVariantRequest;
import com.carrental.dto.response.CarVariantResponse;
import com.carrental.dto.response.PageResponse;
import com.carrental.entity.CarCompany;
import com.carrental.entity.CarVariant;
import com.carrental.repository.CarCompanyRepository;
import com.carrental.repository.CarVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarVariantService {

    private final CarVariantRepository carVariantRepository;
    private final CarCompanyRepository carCompanyRepository;

    public CarVariantResponse create(CarVariantRequest request) {
        CarCompany company = carCompanyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Kompaniya topilmadi!"));

        CarVariant variant = new CarVariant();
        variant.setModel(request.getModel());
        variant.setVariantName(request.getVariantName());
        variant.setYear(request.getYear());
        variant.setFuelType(request.getFuelType().name());
        variant.setTransmission(request.getTransmission().name());
        variant.setSeatingCapacity(request.getSeatingCapacity());
        variant.setCompany(company);

        CarVariant saved = carVariantRepository.save(variant);
        return mapToResponse(saved);
    }

    // FR-ADMIN-004: Paginatsiya va qidiruv bilan ro'yxatni olish
    public PageResponse<CarVariantResponse> getAll(Long companyId, String search, int page, int size) {
        int offset = page * size;
        List<CarVariant> variants = carVariantRepository.findAllPaginatedNative(companyId, search, size, offset);
        long totalElements = carVariantRepository.countByFiltersNative(companyId, search);

        List<CarVariantResponse> content = variants.stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponse.<CarVariantResponse>builder()
                .content(content)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .number(page)
                .size(size)
                .build();
    }

    @Transactional
    public CarVariantResponse update(Long id, CarVariantRequest request) {
        CarVariant variant = carVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant topilmadi!"));

        CarCompany company = carCompanyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Kompaniya topilmadi!"));

        variant.setModel(request.getModel());
        variant.setVariantName(request.getVariantName());
        variant.setYear(request.getYear());
        variant.setFuelType(request.getFuelType().name());
        variant.setTransmission(request.getTransmission().name());
        variant.setSeatingCapacity(request.getSeatingCapacity());
        variant.setCompany(company);

        return mapToResponse(carVariantRepository.save(variant));
    }

    public void delete(Long id) {
        if (!carVariantRepository.existsById(id)) {
            throw new RuntimeException("Variant topilmadi!");
        }
        carVariantRepository.deleteVariantNative(id);
    }

    private CarVariantResponse mapToResponse(CarVariant variant) {
        return CarVariantResponse.builder()
                .id(variant.getId())
                .model(variant.getModel())
                .variantName(variant.getVariantName())
                .year(variant.getYear())
                .fuelType(variant.getFuelType())
                .transmission(variant.getTransmission())
                .seatingCapacity(variant.getSeatingCapacity())
                .companyName(variant.getCompany().getName())
                .companyId(variant.getCompany().getId())
                .build();
    }
}