package com.senla.courses.security;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByName(username).orElseThrow(()
                -> new NotFoundException("Не нашли пользователя по имени"));
        return SecurityUser.fromUser(user);
    }
}
