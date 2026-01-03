package com.carrental.service;

import com.carrental.dto.request.BookingRequest;
import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.Booking;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.enums.BookingStatus;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    @Transactional
    public Booking createBooking(BookingRequest request, User user) {
        // 1. Vaqtni tekshirish (End > Start)
        if (!request.getDropOffTime().isAfter(request.getPickUpTime())) {
            throw new RuntimeException("Qaytarish vaqti olish vaqtidan keyin bo'lishi shart");
        }

        // 2. Konfliktni tekshirish (Native Query orqali)
        long overlaps = bookingRepository.countOverlappingBookings(
                request.getCarId(), request.getPickUpTime(), request.getDropOffTime());

        if (overlaps > 0) {
            throw new RuntimeException("Kechirasiz, bu mashina tanlangan vaqt oralig'ida allaqachon band qilingan.");
        }

        // 3. Mashinani olish va narxni hisoblash
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Mashina topilmadi"));

        double totalAmount = calculateTotalAmount(car, request);

        // 4. Bookingni saqlash
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setPickUpTime(request.getPickUpTime());
        booking.setDropOffTime(request.getDropOffTime());
        booking.setPickUpLocation(request.getPickUpLocation());
        booking.setDropOffLocation(request.getDropOffLocation());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    private double calculateTotalAmount(Car car, BookingRequest request) {
        long totalHours = Duration.between(request.getPickUpTime(), request.getDropOffTime()).toHours();
        if (totalHours < 24) {
            return totalHours * car.getHourlyRate();
        } else {
            double days = Math.ceil(totalHours / 24.0);
            return days * car.getDailyRate();
        }
    }

    // Mijozning barcha bronlarini olish
    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findAllByUserIdNative(userId);
        return bookings.stream()
                .map(BookingResponse::fromEntity)
                .toList();
    }

    // Bronni bekor qilish (Xavfsizlik tekshiruvi bilan)
    @Transactional
    public void cancelBooking(Long bookingId, Long userId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Bron topilmadi"));

        // Xavfsizlik: Bron boshqa foydalanuvchiga tegishli emasligini tekshirish
        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Sizda ushbu bronni bekor qilish huquqi yo'q!");
        }

        // Faqat PENDING yoki CONFIRMED statusdagilarni bekor qilish mumkin
        if (booking.getStatus() == BookingStatus.ACTIVE || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Boshlangan yoki yakunlangan ijarani bekor qilib bo'lmaydi.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        bookingRepository.save(booking);
    }

    public BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .carName(booking.getCar().getVariant().getModel() + " " + booking.getCar().getVariant().getVariantName())
                .registrationNumber(booking.getCar().getRegistrationNumber())
                .pickUpTime(booking.getPickUpTime())
                .dropOffTime(booking.getDropOffTime())
                .pickUpLocation(booking.getPickUpLocation())
                .dropOffLocation(booking.getDropOffLocation())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .build();
    }
}