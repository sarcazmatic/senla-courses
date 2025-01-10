package com.senla.courses.controller.all;

import com.senla.courses.dto.LoginDTO;
import com.senla.courses.service.login.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("")
@AllArgsConstructor
public class PublicLoginController {

    private final LoginService loginService;

    @PostMapping("/log")
    public void login(@RequestBody LoginDTO loginDTO) {
        loginService.login(loginDTO);
    }

}
