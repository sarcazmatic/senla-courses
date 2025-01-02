package com.senla.courses.service.students;

import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.model.StudentsCourses;

import java.util.List;

public interface StudentService {

    Long registerStudent(UserDTO userDTO);

    StudentDTO updateStudent(StudentDTO studentDTO, Long id);

    StudentDTO findById(Long id);

    List<UserDTO> findStudentsByName(String name, int from, int size);

    void deleteStudent(Long id);

    Long registerCourseRequest(Long studentId, Long courseId);

    StudentsCourses findStudentsCoursesById(Long studentId, Long courseId);


}
