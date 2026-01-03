package com.carrental.service;

import com.carrental.entity.User;
import com.carrental.enums.ProfileStatus;
import com.carrental.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public String approveUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));

        if (user.getStatus() != ProfileStatus.PENDING_ADMIN_APPROVAL) {
            throw new RuntimeException("Foydalanuvchi hali barcha verifikatsiyalardan o'tmagan!");
        }

        user.setStatus(ProfileStatus.ACTIVE);
        user.setVerified(true);
        user.setActive(true);
        userRepository.save(user);

        return "Foydalanuvchi " + username + " muvaffaqiyatli tasdiqlandi!";
    }

    @Transactional
    public String rejectUser(String username, String reason) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + username));

        user.setStatus(ProfileStatus.REJECTED);
        userRepository.save(user);

        return "Foydalanuvchi " + username + " rad etildi. Sabab: " + reason;
    }
}