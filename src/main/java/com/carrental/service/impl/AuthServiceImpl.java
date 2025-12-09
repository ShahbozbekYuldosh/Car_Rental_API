package com.carrental.service.impl;

import com.carrental.config.JwtTokenProvider;
import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.AuthResponse;
import com.carrental.entity.RoleEntity;
import com.carrental.entity.UserEntity;
import com.carrental.enums.ProfileStatus; // ProfileStatus importi kerak
import com.carrental.enums.RoleType;
import com.carrental.exception.BadRequestException;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.repository.RoleRepository;
import com.carrental.repository.UserRepository;
import com.carrental.service.AuthService;
import com.carrental.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // request.getEmail() va request.getPhone() o'rniga, request.getUsername() ni tekshiramiz
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Bu username (email yoki telefon) allaqachon ro'yxatdan o'tgan");
        }

        // Yangi user yaratish
        UserEntity user = UserEntity.builder()
                // username (email yoki phone saqlanadi)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                // firstName, lastName yo'q, fullName bor
                .fullName(request.getFullName())
                // Phone va Email tekshiruvi hozircha yo'q, faqat Username bor.
                .drivingLicenseNumber(request.getDriverLicenseNumber())
                // isEmailVerified (avvalgi verified)
                .isEmailVerified(false)
                // status (avvalgi active)
                .status(ProfileStatus.IN_PROGRESS)
                .verificationToken(UUID.randomUUID().toString()) // UserEntity ga qo'shish shart
                .build();

        // Default role qo'shish (CUSTOMER)
        // roleRepository.findByName -> roleRepository.findByRoles ga o'zgartirildi
        RoleEntity customerRole = roleRepository.findByRoles(RoleType.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Role topilmadi"));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(customerRole);
        user.setRoles(roles);

        userRepository.save(user);

        // Verification email yuborish
        emailService.sendVerificationEmail(user.getUsername(), user.getVerificationToken());

        // JWT token generatsiya qilish
        String token = tokenProvider.generateToken(user.getUsername()); // user.getEmail() -> user.getUsername()

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                // user.getEmail() -> user.getUsername()
                .username(user.getUsername())
                // firstName, lastName o'rniga fullName
                .fullName(user.getFullName())
                // role.getName().name() -> role.getRoles().name()
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoles().name())
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // request.getEmail() o'rniga LoginRequest.getUsername() ishlatilishi kerak.
        // Agar LoginRequest da faqat email bo'lsa, uni username deb qabul qilamiz.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), // request.getEmail() -> request.getUsername()
                        request.getPassword()
                )
        );

        // User ma'lumotlarini olish
        // findByEmailWithRoles -> findByUsernameWithRoles ga o'zgartirildi
        UserEntity user = userRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi"));

        // JWT token generatsiya qilish
        String token = tokenProvider.generateToken(user.getUsername()); // user.getEmail() -> user.getUsername()

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                // email -> username, firstName/lastName -> fullName, getName -> getRoles
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roles(user.getRoles().stream()
                        .map(role -> role.getRoles().name())
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        // User -> UserEntity
        UserEntity user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Noto'g'ri verification token"));

        // user.setVerified(true) -> user.setIsEmailVerified(true)
        user.setIsEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        // UserRepository da findByEmail ni yaratish kerak yoki findByUsername dan foydalanish
        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi"));

        String resetToken = UUID.randomUUID().toString();
        // user.setResetPasswordToken(resetToken) (UserEntity ga qo'shish shart)
        user.setResetPasswordToken(resetToken);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // User -> UserEntity
        UserEntity user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BadRequestException("Noto'g'ri reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}