package com.senla.courses.service.teachers;

import com.senla.courses.dto.user.UserDTO;

import java.util.List;

public interface TeacherService {

    Long registerTeacher(UserDTO userDTO);

    UserDTO updateTeacher(UserDTO userDTO, Long id);

    UserDTO findById(Long id);

    List<UserDTO> findTeachersByName(String name, int from, int size);

    void deleteTeacher(Long id);
}
