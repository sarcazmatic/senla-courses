package com.senla.courses.service.students;

import com.senla.courses.dto.user.UserDTO;

import java.util.List;

public interface StudentService {

    Long registerStudent(UserDTO userDTO);

    UserDTO updateStudent(UserDTO userDTO);

    UserDTO findStudent(Long id);

    List<UserDTO> findStudents(String name, int from, int size);

    void deleteStudent(Long id);
}
