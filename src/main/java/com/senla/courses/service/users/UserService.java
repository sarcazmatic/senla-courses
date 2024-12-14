package com.senla.courses.service.users;

import com.senla.courses.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Long registerUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO findById(Long id);

    List<UserDTO> findUsersByName(String name, int from, int size);

    void deleteUser(Long id);
}
