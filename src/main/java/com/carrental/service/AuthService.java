package com.carrental.service;

import com.carrental.config.CustomUserDetails;
import com.carrental.config.jwt.JwtUtils;
import com.carrental.dto.request.LoginRequest;
import com.carrental.dto.request.RegisterRequest;
import com.carrental.dto.response.JwtResponse;
import com.carrental.dto.response.UserResponse;
import com.carrental.entity.RefreshToken;
import com.carrental.entity.Role;
import com.carrental.entity.User;
import com.carrental.enums.ProfileStatus;
import com.carrental.enums.RoleName;
import com.carrental.mapper.UserMapper;
import com.carrental.repository.RoleRepository;
import com.carrental.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final StringRedisTemplate redisTemplate;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public UserResponse register(RegisterRequest request, MultipartFile passport, MultipartFile license) {
        if (userRepository.existsByUsernameNative(request.getUsername())) {
            throw new RuntimeException("Xato: Username allaqachon mavjud!");
        }
        if (userRepository.existsByEmailNative(request.getEmail())) {
            throw new RuntimeException("Xato: Email allaqachon ro'yxatdan o'tgan!");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setActive(false);
        user.setVerified(false);
        user.setStatus(ProfileStatus.REGISTRATION_PROGRESS);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        user.setEmailCode(generateOTP());
        user.setSmsCode(generateOTP());

        user.setSmsCodeSentAt(LocalDateTime.now());
        user.setEmailCodeSentAt(LocalDateTime.now());

        Role customerRole = roleRepository.findByNameNative(RoleName.ROLE_CUSTOMER.name())
                .orElseThrow(() -> new RuntimeException("Xato: Role topilmadi"));
        user.getRoles().add(customerRole);

        user.setPassportImageUrl(fileService.saveImage(passport, "passports"));
        user.setLicenseImageUrl(fileService.saveImage(license, "licenses"));

        User savedUser = userRepository.save(user);
        sendVerificationLog(savedUser);

        kafkaProducerService.sendMessage("user-registration-topic", "Yangi foydalanuvchi ro'yxatdan o'tdi: " + savedUser.getUsername());
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("Xato: Foydalanuvchi topilmadi!"));

        if (user.getStatus() != ProfileStatus.ACTIVE) {
            throw new RuntimeException("Xato: Profilingiz faol emas. Joriy holat: " + user.getStatus());
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Xato: Parol noto'g'ri!");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        user.setActive(true);
        userRepository.save(user);

        String jwt = jwtUtils.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return JwtResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Transactional
    public String verifyEmail(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        // REDISDA URINISHLARNI TEKSHIRAMIZ
        String limitKey = "auth:limit:email:" + email;
        checkRateLimit(limitKey);

        if (user.getEmailCodeSentAt() == null ||
                user.getEmailCodeSentAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Email kodi muddati tugagan (5 daqiqa o'tib ketgan).");
        }

        if (!user.getEmailCode().equals(code)) {
            // REDISDA URINISH SONINI OSHIRAMIZ
            increaseRateLimit(limitKey);
            throw new RuntimeException("Email kodi noto'g'ri. Urinishlar soni oshdi.");
        }

        user.setEmailVerified(true);
        user.setEmailCode(null);
        redisTemplate.delete(limitKey); // Muvaffaqiyatli bo'lsa limitni o'chiramiz

        checkAndUpgradeStatus(user);
        userRepository.save(user);

        return "Email muvaffaqiyatli tasdiqlandi!";
    }

    @Transactional
    public void verifyPhone(String phone, String code) {
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi"));

        // REDISDA URINISHLARNI TEKSHIRAMIZ
        String limitKey = "auth:limit:phone:" + phone;
        checkRateLimit(limitKey);

        if (user.getSmsCodeSentAt() == null ||
                user.getSmsCodeSentAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Kodning amal qilish muddati tugagan (5 daqiqa o'tdi).");
        }

        if (!user.getSmsCode().equals(code)) {
            // REDISDA URINISH SONINI OSHIRAMIZ
            increaseRateLimit(limitKey);
            throw new RuntimeException("Tasdiqlash kodi noto'g'ri.");
        }

        user.setPhoneVerified(true);
        user.setSmsCode(null);
        redisTemplate.delete(limitKey); // Muvaffaqiyatli bo'lsa limitni o'chiramiz

        checkAndUpgradeStatus(user); // Telefon tasdiqlangach ham statusni tekshirish kerak
        userRepository.save(user);
    }

    private void checkRateLimit(String key) {
        String attempts = redisTemplate.opsForValue().get(key);
        if (attempts != null && Integer.parseInt(attempts) >= 5) {
            throw new RuntimeException("Urinishlar soni tugadi (5/5). 5 daqiqadan keyin qayta urining.");
        }
    }

    private void increaseRateLimit(String key) {
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(5));
    }

    private void checkAndUpgradeStatus(User user) {
        if (user.isEmailVerified() && user.isPhoneVerified()) {
            user.setStatus(ProfileStatus.PENDING_ADMIN_APPROVAL);
            log.info("Foydalanuvchi {} endi admin tasdig'ini kutmoqda.", user.getUsername());
        }
    }

    private String generateOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendVerificationLog(User user) {
        log.info("📧 EMAIL VERIFICATION [{}]: Kod -> {}", user.getEmail(), user.getEmailCode());
        log.info("📱 SMS VERIFICATION [{}]: Kod -> {}", user.getPhoneNumber(), user.getSmsCode());
    }

    @Transactional
    public void logout(Long userId) {
        userRepository.updateUserStatusNative(userId, false);
        refreshTokenService.deleteByUserIdNative(userId);
        SecurityContextHolder.clearContext();
    }

    public JwtResponse refreshToken(String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtils.generateToken(new CustomUserDetails(user));
                    return JwtResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(requestRefreshToken)
                            .user(userMapper.toResponse(user))
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token topilmadi!"));
    }
}