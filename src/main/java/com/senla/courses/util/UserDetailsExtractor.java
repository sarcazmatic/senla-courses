package com.senla.courses.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserDetailsExtractor {

    public static User extractUserDetails() {
        User user;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        } catch (ClassCastException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
