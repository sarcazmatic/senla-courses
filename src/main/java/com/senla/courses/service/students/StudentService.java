package com.senla.courses.service.students;

import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.dto.UserDTO;

import java.util.List;

public interface StudentService {

    Long registerStudent(UserDTO userDTO);

    StudentDTO updateStudent(StudentDTO studentDTO, Long id);

    StudentDTO findById(Long id);

    List<UserDTO> findStudentsByName(String name, int from, int size);

    void deleteStudent(Long id);

    Long registerCourseRequest(Long studentId, Long courseId);

    StudentsCoursesDTO findStudentsCoursesById(Long studentId, Long courseId);

    List<StudentsCoursesDTO> findStudentsCoursesByCourseId(Long courseId);

    Integer updateRequest(Long courseId, List<Long> ids, String response);


}
