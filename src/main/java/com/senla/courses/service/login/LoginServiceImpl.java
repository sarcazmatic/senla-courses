package com.senla.courses.service.login;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.LoginDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.model.User;
import com.senla.courses.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserDAO userDAO;
    private final DaoAuthenticationProvider authenticationProvider;


    @Override
    public void login(LoginDTO loginDTO) {
        User userAuth = userDAO.findByLogin(loginDTO.getLogin()).orElseThrow(()
        -> new NotFoundException("Не нашли пользователя по логину"));
        if (!userAuth.getPassword().equals("{noop}" + loginDTO.getPassword())) {
            throw new ValidationException("Пароли не совпали");
        }
        UsernamePasswordAuthenticationToken reqWithLoginAndPAss
                = new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword());
        Authentication auth = authenticationProvider.authenticate(reqWithLoginAndPAss);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }
}
