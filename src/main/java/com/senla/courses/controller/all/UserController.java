package com.senla.courses.controller.all;

import com.senla.courses.dto.user.UserDTO;
import com.senla.courses.service.users.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long registerUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UserDTO> findUsers(@RequestParam(required = false, name = "name") String name,
                                   @RequestParam (required = false, defaultValue = "1") int from,
                                   @RequestParam (required = false, defaultValue = "10") int size) {
        return userService.findUsers(name, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserDTO findUser(@PathVariable("id") Long id) {
        return userService.findUser(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

}
