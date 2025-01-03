package com.senla.courses.service.students;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dao.StudentCoursesDAO;
import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.mapper.StudentMapper;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.mapper.StudentsCoursesMapper;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Course;
import com.senla.courses.model.Student;
import com.senla.courses.model.StudentsCourses;
import com.senla.courses.model.StudentsCoursesPK;
import com.senla.courses.model.User;
import com.senla.courses.util.enums.StudentCourseRequestEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final StudentCoursesDAO studentCoursesDAO;
    private final StudentsCoursesMapper studentsCoursesMapper;


    @Override
    public Long registerStudent(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);
        user.setDateTimeRegistered(LocalDateTime.now());
        Long userPk = userDAO.save(user);
        User userStudent = userDAO.find(userPk)
                .orElseThrow(() -> new RuntimeException("Не смогли найти такого пользовтеля"));
        Student student = Student.builder()
                .id(userPk)
                .user(userStudent)
                .rating(0.0)
                .build();
        return studentDAO.save(student);
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO, Long id) {
        Student student = studentDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли студента по id " + id));
        Student studentUpd = studentMapper.updateStudent(student, studentDTO);
        if (studentDTO.getUserDTO() != null) {
            User user = userMapper.updateUser(student.getUser(), studentDTO.getUserDTO());
            studentUpd.setUser(user);
        }
        studentDAO.update(studentUpd);
        return studentMapper.fromStudent(studentUpd);
    }

    @Override
    public StudentDTO findById(Long id) {
        Student student = studentDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли студента по id " + id));
        return studentMapper.fromStudent(student);
    }

    @Override
    public List<UserDTO> findStudentsByName(String name, int from, int size) {
        List<UserDTO> userDTOList = studentDAO.findAllByText(name, from, size)
                .stream()
                .map(s -> userMapper.fromUser(s.getUser()))
                .toList();
        if (userDTOList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return userDTOList;
    }

    @Override
    public void deleteStudent(Long id) {
        studentDAO.deleteById(id);
    }

    @Override
    public Long registerCourseRequest(Long studentId, Long courseId) {
        Course course = courseDAO.find(courseId).orElseThrow(()
                -> new NotFoundException("Не нашли курс по id " + courseId));
        Student student = studentDAO.find(studentId).orElseThrow(()
                -> new NotFoundException("Не нашли студента по id " + studentId));
        StudentsCoursesPK studentCoursesPK = new StudentsCoursesPK(student.getId(), course.getId());
        StudentsCourses studentsCourses = StudentsCourses.builder()
                .course(course)
                .student(student)
                .courseStatus(StudentCourseRequestEnum.REQUESTED)
                .build();
        studentsCourses.setId(studentCoursesPK);
        return studentCoursesDAO.save(studentsCourses);
    }

    @Override
    public StudentsCoursesDTO findStudentsCoursesById(Long studentId, Long courseId) {
        return studentsCoursesMapper.fromStudentCourses(studentCoursesDAO.findByIds(studentId, courseId));
    }

    @Override
    public List<StudentsCoursesDTO> findStudentsCoursesByCourseId(Long courseId) {
        return studentCoursesDAO.findAllByCourseId(courseId).stream().map(studentsCoursesMapper::fromStudentCourses).toList();
    }

    @Override
    public Integer updateRequest(Long courseId, List<Long> ids, String response) {
        if (response.toUpperCase().equals(StudentCourseRequestEnum.APPROVED.toString())
                || response.toUpperCase().equals(StudentCourseRequestEnum.DECLINED.toString())) {
            StudentCourseRequestEnum newResponse = StudentCourseRequestEnum.valueOf(response.toUpperCase());
            return studentCoursesDAO.updateRequest(courseId, ids, newResponse);
        } else {
            throw new ValidationException("Передано неверное значение response -- " + response);
        }
    }

}
