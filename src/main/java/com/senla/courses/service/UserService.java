package com.senla.courses.service;

import com.senla.courses.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Long registerUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO findUser(Long id);

    List<UserDTO> findUsers(String name, int from, int size);

    void deleteUser(Long id);
}
