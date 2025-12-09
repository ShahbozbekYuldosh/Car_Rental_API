package com.carrental.controller;

import com.carrental.dto.request.ReviewRequest;
import com.carrental.dto.response.ApiResponse;
import com.carrental.dto.response.ReviewResponse;
import com.carrental.entity.ReviewEntity;
import com.carrental.repository.ReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> createReview(@Valid @RequestBody ReviewRequest request) {
        // Review creation logic
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review qo'shildi"));
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getCarReviews(@PathVariable Long carId) {
        List<ReviewEntity> reviews = reviewRepository.findApprovedReviewsByCarId(carId);
        List<ReviewResponse> response = reviews.stream()
                .map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .userName(r.getUser().getFullName())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveReview(@PathVariable Long id) {
        // Approve review logic
        return ResponseEntity.ok(ApiResponse.success("Review tasdiqlandi"));
    }
}