package com.senla.courses.service.students;

import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.dto.UserDTO;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface StudentService {

    Long registerStudent(UserDTO userDTO);

    StudentDTO updateStudent(User user, StudentDTO studentDTO, Long id);

    StudentDTO findById(Long id);

    List<UserDTO> findStudentsByName(String name, int from, int size);

    void deleteStudent(User user, Long id);

    Long registerCourseRequest(User user, Long studId, Long courseId);

    StudentsCoursesDTO findStudentsCoursesById(User user, Long studId, Long courseId);

    List<StudentsCoursesDTO> findStudentsCoursesByCourseId(Long courseId);

    Integer updateRequest(Long courseId, List<Long> ids, String response);


}
