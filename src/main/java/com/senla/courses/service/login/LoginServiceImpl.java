package com.senla.courses.service.login;

import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.LoginDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.User;
import lombok.AllArgsConstructor;
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
        UsernamePasswordAuthenticationToken reqWithLoginAndPAss
                = new UsernamePasswordAuthenticationToken(userAuth.getLogin(), loginDTO.getPassword());
        Authentication auth = authenticationProvider.authenticate(reqWithLoginAndPAss);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }
}
