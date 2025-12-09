package com.carrental.service.impl;

import com.carrental.dto.request.BookingCreateRequest;
import com.carrental.dto.response.BookingResponse;
import com.carrental.entity.BookingEntity;
import com.carrental.entity.CarEntity;
import com.carrental.entity.LocationEntity;
import com.carrental.entity.UserEntity;
import com.carrental.enums.BookingStatus;
import com.carrental.enums.CarStatus;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.LocationRepository;
import com.carrental.repository.UserRepository;
import com.carrental.service.BookingService;
import com.carrental.utils.PriceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PriceCalculator priceCalculator;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request, Long userId) {
        // Validatsiyalar
        if (request.getReturnDate().isBefore(request.getPickupDate())) {
            throw new BadRequestException("Qaytarish sanasi olish sanasidan kechroq bo'lishi kerak");
        }

        // User tekshirish
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi"));

        // Mashina tekshirish
        CarEntity car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Mashina topilmadi"));

        // Mashina available ekanligini tekshirish
        if (!checkAvailability(request.getCarId(), request)) {
            throw new BadRequestException("Mashina bu vaqtda band");
        }

        // Location tekshirish
        LocationEntity pickupLocation = locationRepository.findById(request.getPickupLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Olish joyi topilmadi"));

        LocationEntity returnLocation = locationRepository.findById(request.getReturnLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Qaytarish joyi topilmadi"));

        // Narxni hisoblash
        long days = ChronoUnit.DAYS.between(request.getPickupDate(), request.getReturnDate());
        if (days < 1) days = 1;

        BigDecimal totalPrice = car.getDailyRate().multiply(BigDecimal.valueOf(days));
        BigDecimal deposit = totalPrice.multiply(BigDecimal.valueOf(0.2)); // 20% deposit

        // Booking yaratish
        BookingEntity booking = BookingEntity.builder()
                .user(user)
                .car(car)
                .pickupDate(request.getPickupDate())
                .returnDate(request.getReturnDate())
                .pickupLocation(pickupLocation)
                .returnLocation(returnLocation)
                .totalPrice(totalPrice)
                .deposit(deposit)
                .status(BookingStatus.PENDING)
                .specialRequests(request.getSpecialRequests())
                .build();

        booking = bookingRepository.save(booking);

        // Mashina statusini o'zgartirish
        car.setStatus(CarStatus.RENTED);
        carRepository.save(car);

        return mapToBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBookingStatus(Long id, BookingStatus status) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking topilmadi"));

        booking.setStatus(status);

        // Agar booking completed yoki cancelled bo'lsa, mashinani available qilish
        if (status == BookingStatus.COMPLETED || status == BookingStatus.CANCELLED) {
            CarEntity car = booking.getCar();
            car.setStatus(CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        booking = bookingRepository.save(booking);
        return mapToBookingResponse(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        updateBookingStatus(id, BookingStatus.CANCELLED);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        BookingEntity booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking topilmadi"));
        return mapToBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkAvailability(Long carId, BookingCreateRequest request) {
        List<BookingEntity> conflictingBookings = bookingRepository.findConflictingBookings(
                carId,
                request.getPickupDate(),
                request.getReturnDate()
        );
        return conflictingBookings.isEmpty();
    }

    private BookingResponse mapToBookingResponse(BookingEntity booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userFullName(booking.getUser().getFullName())
                .carId(booking.getCar().getId())
                .carBrand(booking.getCar().getBrand())
                .carModel(booking.getCar().getModel())
                .pickupDate(booking.getPickupDate())
                .returnDate(booking.getReturnDate())
                .pickupLocation(booking.getPickupLocation().getName())
                .returnLocation(booking.getReturnLocation().getName())
                .totalPrice(booking.getTotalPrice())
                .deposit(booking.getDeposit())
                .status(booking.getStatus())
                .specialRequests(booking.getSpecialRequests())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
