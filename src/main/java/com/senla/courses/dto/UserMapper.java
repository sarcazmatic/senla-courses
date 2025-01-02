package com.senla.courses.dto;

import com.senla.courses.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromUserDTO (UserDTO userDTO) {
        return User.builder()
                .age(userDTO.getAge())
                .description(userDTO.getDescription())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .build();
    }

    public UserDTO fromUser (User user) {
        return UserDTO.builder()
                .age(user.getAge())
                .description(user.getDescription())
                .email(user.getEmail())
                .name(user.getName())
                .dateTimeRegistered(user.getDateTimeRegistered())
                .password(user.getPassword())
                .build();
    }
}
