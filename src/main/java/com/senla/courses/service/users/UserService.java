package com.senla.courses.service.users;

import com.senla.courses.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    Long registerUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO findUser(Long id);

    List<UserDTO> findUsers(String name, int from, int size);

    void deleteUser(Long id);
}
