package com.senla.courses.service;

import com.senla.courses.dto.UserDTO;

public interface UserService {

    Long registerUser(UserDTO userDTO);

    UserDTO findUser(Long id);

}
