package com.senla.courses.controller;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.model.User;
import com.senla.courses.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        System.out.println(user);
        return user;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.FOUND)
    public User findUser() {
        User user = new User();
        return user;
    }

}
