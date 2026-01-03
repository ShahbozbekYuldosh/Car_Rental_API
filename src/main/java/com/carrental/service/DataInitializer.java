package com.carrental.service;

import com.carrental.entity.Role;
import com.carrental.entity.User;
import com.carrental.enums.ProfileStatus;
import com.carrental.enums.RoleName;
import com.carrental.repository.RoleRepository;
import com.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = createRoleIfNotFound(RoleName.ROLE_ADMIN);
        createRoleIfNotFound(RoleName.ROLE_CUSTOMER);

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@carrental.com");
            admin.setPassword(passwordEncoder.encode("admin123!"));
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setPhoneNumber("+998901234567");
            admin.setDriverLicenseNumber("AA1234567");
            admin.setAddress("Tashkent, Uzbekistan");

            admin.setStatus(ProfileStatus.ACTIVE);
            admin.setActive(true);
            admin.setVerified(true);
            admin.setPhoneVerified(true);
            admin.setEmailVerified(true);

            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            System.out.println(">>> [OK] Default ADMIN muvaffaqiyatli yaratildi!");
        }
    }

    private Role createRoleIfNotFound(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }
}