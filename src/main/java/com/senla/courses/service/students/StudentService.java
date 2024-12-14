package com.senla.courses.service.students;

import com.senla.courses.dto.UserDTO;

import java.util.List;

public interface StudentService {

    Long registerStudent(UserDTO userDTO);

    UserDTO updateStudent(UserDTO userDTO);

    UserDTO findById(Long id);

    List<UserDTO> findStudentsByName(String name, int from, int size);

    void deleteStudent(Long id);
}
