package com.carrental.service;

import com.carrental.dto.request.CarCompanyRequest;
import com.carrental.dto.response.CarCompanyResponse;
import com.carrental.dto.response.PageResponse;
import com.carrental.entity.CarCompany;
import com.carrental.repository.CarCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarCompanyService {

    private final CarCompanyRepository carCompanyRepository;

    public CarCompany create(CarCompanyRequest request) {
        if (carCompanyRepository.existsByNameNative(request.getName())) {
            throw new RuntimeException("Bunday kompaniya allaqachon mavjud!");
        }
        CarCompany company = new CarCompany();
        company.setName(request.getName());
        company.setCountry(request.getCountry());
        company.setWebsite(request.getWebsite());
        return carCompanyRepository.save(company);
    }

    public PageResponse<CarCompanyResponse> getAll(String name, int page, int size) {
        int offset = page * size;

        // Ma'lumotlarni olish
        List<CarCompany> companies = carCompanyRepository.findAllPaginatedNative(name, size, offset);

        // Umumiy sonini olish
        long totalElements = carCompanyRepository.countByNameNative(name);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // DTO ga o'girish
        List<CarCompanyResponse> content = companies.stream()
                .map(c -> CarCompanyResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .country(c.getCountry())
                        .website(c.getWebsite())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();

        return PageResponse.<CarCompanyResponse>builder()
                .content(content)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .number(page)
                .size(size)
                .build();
    }

    public void update(Long id, CarCompanyRequest request) {
        carCompanyRepository.updateCompanyNative(id, request.getName(), request.getCountry(), request.getWebsite());
    }

    public void delete(Long id) {
        carCompanyRepository.deleteById(id);
    }
}