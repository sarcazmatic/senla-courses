package com.senla.courses.service.teachers;

import com.senla.courses.dto.UserDTO;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface TeacherService {

    Long registerTeacher(UserDTO userDTO);

    UserDTO updateTeacher(User userSec, UserDTO userDTO, Long id);

    UserDTO findById(Long id);

    List<UserDTO> findTeachersByName(String name, int from, int size);

    void deleteTeacher(User userSec, Long id);
}
