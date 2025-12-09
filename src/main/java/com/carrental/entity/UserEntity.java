package com.carrental.entity;

import com.carrental.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class UserEntity extends  BaseEntity{

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true,  nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "driving_license_number")
    private String drivingLicenseNumber;

    private LocalDateTime licenseExpiryDate;

    private String address;

    private String city;

    private String country;

    private boolean isEmailVerified =  false;

    private boolean isPhoneVerified =  false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @Column(name = "status")
    private ProfileStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BookingEntity> bookings = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ReviewEntity> reviews = new HashSet<>();

}
