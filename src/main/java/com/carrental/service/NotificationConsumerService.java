package com.carrental.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumerService {

    @KafkaListener(topics = "user-registration-topic", groupId = "car-rental-group")
    public void consumeRegistration(String message) {
        log.info("📢 [KAFKA CONSUMER]: Yangi foydalanuvchi haqida xabar keldi -> {}", message);
    }
}