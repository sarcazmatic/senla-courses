package com.senla.courses.service.login;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.LoginDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.model.User;
import com.senla.courses.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserDAO userDAO;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public void login(LoginDTO loginDTO) {
        System.out.println(loginDTO.getLogin());
        User userAuth = userDAO.findByLogin(loginDTO.getLogin()).orElseThrow(()
        -> new NotFoundException("Не нашли пользователя по логину"));
        if (!userAuth.getPassword().equals("{noop}" + loginDTO.getPassword())) {
            throw new ValidationException("Пароли не совпали");
        }
        customUserDetailsService.loadUserByUsername(loginDTO.getLogin());
    }
}
