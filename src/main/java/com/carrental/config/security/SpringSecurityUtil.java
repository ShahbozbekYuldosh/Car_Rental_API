package com.carrental.config.security;

import com.carrental.config.CustomUserDetails;
import com.carrental.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

    public static User getCurrentEntity() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();
        return user.getUser();
    }

    public static Long getCurrentUserId() {
        return getCurrentEntity().getId();
    }
}