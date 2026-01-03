package com.carrental.service;

import com.carrental.entity.Booking;
import com.carrental.entity.Payment;
import com.carrental.enums.BookingStatus;
import com.carrental.enums.CarStatus;
import com.carrental.enums.PaymentMethod;
import com.carrental.enums.PaymentStatus;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Transactional
    public void processPayment(Long bookingId, String transactionId, String method) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Bron topilmadi"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Ushbu bron uchun to'lov qilib bo'lmaydi");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setTransactionId(transactionId);
        payment.setPaymentMethod(PaymentMethod.valueOf(method.toUpperCase()));
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        booking.setStatus(BookingStatus.CONFIRMED);

        booking.getCar().setStatus(CarStatus.RENTED);

        bookingRepository.save(booking);
        notificationService.sendBookingConfirmation(booking.getUser(), booking);
    }
}