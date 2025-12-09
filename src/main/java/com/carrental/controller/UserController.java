package com.carrental.controller;

import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.UserResponse;
import com.carrental.entity.UserEntity;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getUsername())
                .driverLicense(user.getDriverLicense())
                .licenseExpiryDate(user.getLicenseExpiryDate())
                .address(user.getAddress())
                .city(user.getCity())
                .country(user.getCountry())
                .verified(user.getVerified())
                .active(user.getStatus())
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoles().name())
                        .collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @RequestBody UserResponse request,
            Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User topilmadi"));


        user.setFullName(request.getFullname());
        user.setPhone(request.getUsername);
        user.setDriverLicense(request.getDriverLicense());
        user.setLicenseExpiryDate(request.getLicenseExpiryDate());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());

        userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .driverLicense(user.getDriverLicense())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Profile yangilandi", response));
    }
}