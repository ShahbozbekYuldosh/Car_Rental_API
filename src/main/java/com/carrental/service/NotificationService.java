package com.carrental.service;

import com.carrental.entity.Booking;
import com.carrental.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Async
    public void sendBookingConfirmation(User user, Booking booking) {
        log.info("Async oqim boshlandi: Thread nomi - {}", Thread.currentThread().getName());

        try {
            Thread.sleep(2000);

            String message = String.format(
                    "Hurmatli %s, sizning %s mashinasi uchun broningiz tasdiqlandi. Umumiy summa: %.2f",
                    user.getFirstName(),
                    booking.getCar().getVariant().getModel(),
                    booking.getTotalAmount()
            );

            log.info("SMS muvaffaqiyatli yuborildi: {}", message);

        } catch (InterruptedException e) {
            log.error("SMS yuborishda xatolik yuz berdi", e);
        }
    }
}