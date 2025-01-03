package com.senla.courses.security;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByName(username).orElseThrow(()
                -> new NotFoundException("Не нашли пользователя по имени"));
        return SecurityUser.fromUser(user);
    }
}
