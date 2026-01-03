package com.carrental.entity;

import com.carrental.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class User extends BaseEntity {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String address;

    @Column(unique = true)
    private String driverLicenseNumber;

    // Rasm URLlari
    private String passportImageUrl;
    private String licenseImageUrl;
    private String profileImageUrl;

    private boolean isVerified = false;
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProfileStatus status;

    private String emailCode;
    private String smsCode;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime smsCodeSentAt;
    private LocalDateTime emailCodeSentAt;
    private Integer phoneVerificationAttempts = 0;
    private Integer emailVerificationAttempts = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}