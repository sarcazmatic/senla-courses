package com.senla.courses.service.teachers;

import com.senla.courses.dto.UserDTO;

import java.util.List;

public interface TeacherService {

    Long registerTeacher(UserDTO userDTO);

    UserDTO updateTeacher(UserDTO userDTO);

    UserDTO findTeacher(Long id);

    List<UserDTO> findTeachers(String name, int from, int size);

    void deleteTeacher(Long id);
}
