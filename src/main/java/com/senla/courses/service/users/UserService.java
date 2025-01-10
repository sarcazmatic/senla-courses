package com.senla.courses.service.users;

import com.senla.courses.dto.UserDTO;


public interface UserService {

    UserDTO findById(Long id);

}
