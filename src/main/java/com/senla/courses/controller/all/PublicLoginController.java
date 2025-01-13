package com.senla.courses.controller.all;

import com.senla.courses.dto.LoginDTO;
import com.senla.courses.service.login.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("")
@AllArgsConstructor
@Slf4j
public class PublicLoginController {

    private final LoginService loginService;

    @PostMapping("/log")
    public void login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        log.info("Пришел запрос на аутентификацию с параметрами логин {}. Эндпоинт {}. Метод {}",
                loginDTO.getLogin(), request.getRequestURL(), request.getMethod());
        loginService.login(loginDTO);
    }

}
