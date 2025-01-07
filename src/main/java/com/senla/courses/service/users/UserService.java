package com.senla.courses.service.users;

import com.senla.courses.dto.UserDTO;


public interface UserService {

    Long registerUser(UserDTO userDTO);

    UserDTO findById(Long id);

}
