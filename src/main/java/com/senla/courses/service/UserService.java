package com.senla.courses.service;

import com.senla.courses.dto.UserDTO;
import com.senla.courses.model.User;

public interface UserService {

    User registerUser(UserDTO userDTO);

}
