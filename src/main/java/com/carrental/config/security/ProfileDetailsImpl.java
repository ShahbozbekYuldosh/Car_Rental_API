package com.carrental.config.security;

import com.carrental.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ProfileDetailsImpl implements UserDetails {
    private Integer id;
    private String username;
    private String fullName;

    @JsonIgnore
    private String password;

    private boolean isEmailVerified;
    private boolean isPhoneVerified;

    UserEntity profile;

    private Collection<? extends GrantedAuthority> authorities;

    public static ProfileDetailsImpl build(UserEntity profile) {
        List<GrantedAuthority> authorities = profile.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoles().name()))
                .collect(Collectors.toList());

        return new ProfileDetailsImpl(
                profile.getId(),
                profile.getUsername(),
                profile.getFullName(),
                profile.getPassword(),
                profile.isEmailVerified(),
                profile.isPhoneVerified(),
                profile,
                authorities);
    }

    @Override
    public boolean isAccountNonLocked() {
        return profile.getStatus() != com.carrental.enums.ProfileStatus.BLOCKED;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}