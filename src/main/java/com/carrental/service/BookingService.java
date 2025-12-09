package com.carrental.service;

import com.carrental.dto.request.BookingCreateRequest;
import com.carrental.dto.response.BookingResponse;
import com.carrental.enums.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest request, Long userId);
    BookingResponse updateBookingStatus(Long id, BookingStatus status);
    void cancelBooking(Long id);
    BookingResponse getBookingById(Long id);
    List<BookingResponse> getUserBookings(Long userId);
    List<BookingResponse> getAllBookings();
    boolean checkAvailability(Long carId, BookingCreateRequest request);
}

