<<<<<<<< HEAD:src/main/java/com/senla/courses/dto/user/UserMapper.java
package com.senla.courses.dto.user;
========
package com.senla.courses.mapper;
>>>>>>>> main:src/main/java/com/senla/courses/mapper/UserMapper.java

import com.senla.courses.dto.UserDTO;
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

    public User updateUser (User user, UserDTO userDTO) {
        if (userDTO.getDateTimeRegistered() != null) {
            user.setDateTimeRegistered(userDTO.getDateTimeRegistered());
        }
        if (userDTO.getAge() != null) {
            user.setAge(userDTO.getAge());
        }
        if (userDTO.getDescription() != null) {
            user.setDescription(userDTO.getDescription());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        return user;
    }

}
