package com.senla.courses.service;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.StudentsCoursesDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.mapper.StudentMapper;
import com.senla.courses.mapper.StudentsCoursesMapper;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Role;
import com.senla.courses.model.Student;
import com.senla.courses.model.StudentsCourses;
import com.senla.courses.model.User;
import com.senla.courses.service.students.StudentServiceImpl;
import com.senla.courses.util.enums.StudentsCoursesRequestEnum;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class StudentsServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private StudentDAO studentDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private CourseDAO courseDAO;
    @Mock
    private StudentsCoursesDAO studentCoursesDAO;
    @Mock
    private StudentsCoursesMapper studentsCoursesMapper;
    @InjectMocks
    private StudentServiceImpl studentService;

    private UserDTO userDTO;
    private StudentDTO studentDTO;
    private User user;
    private Student student;
    private Course course;
    private StudentsCourses studentsCourses;

    private org.springframework.security.core.userdetails.User userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = UserDTO.builder()
                .login("testUser")
                .name("Имя студента 1")
                .email("user2@test.ru")
                .build();

        user = User.builder()
                .age(25)
                .id(12L)
                .name("Имя студента 1")
                .description("Тестовое описание юзера")
                .email("user2@test.ru")
                .password("{noop}123321")
                .login("testUser")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.STUDENT)
                .build();

        studentDTO = StudentDTO.builder()
                .userDTO(userDTO)
                .rating(4.5)
                .build();

        student = Student.builder()
                .id(12L)
                .user(user)
                .rating(4.5)
                .build();

        userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                user.getRole().getAuthorities());

        course = Course.builder()
                .id(1L)
                .name("Курс тест 1")
                .description("Описание курса 1")
                .field("Поле курса 1")
                .complexity(3)
                .duration(6)
                .teachers(new HashSet<>())
                .modules(new HashSet<>())
                .build();

        studentsCourses = StudentsCourses.builder()
                .student(student)
                .course(course)
                .courseStatus(StudentsCoursesRequestEnum.REQUESTED)
                .build();

        ReflectionTestUtils.setField(studentService, "studentDAO", studentDAO);
        ReflectionTestUtils.setField(studentService, "courseDAO", courseDAO);
        ReflectionTestUtils.setField(studentService, "userMapper", userMapper);
        ReflectionTestUtils.setField(studentService, "studentMapper", studentMapper);
        ReflectionTestUtils.setField(studentService, "studentCoursesDAO", studentCoursesDAO);
        ReflectionTestUtils.setField(studentService, "userDAO", userDAO);
        ReflectionTestUtils.setField(studentService, "studentsCoursesMapper", studentsCoursesMapper);
    }

    @Test
    public void testRegisterStudent_Success() {
        when(userMapper.fromUserDTO(userDTO)).thenReturn(user);
        when(userDAO.save(user)).thenReturn(1L);
        when(userDAO.find(1L)).thenReturn(Optional.of(user));
        when(studentDAO.save(any(Student.class))).thenReturn(1L);

        Long studentId = studentService.registerStudent(userDTO);

        assertEquals(1L, studentId);
        verify(userDAO, times(1)).save(user);
        verify(studentDAO, times(1)).save(any(Student.class));
    }

    @Test
    public void testRegisterStudent_UserNotFound() {
        when(userMapper.fromUserDTO(userDTO)).thenReturn(user);
        when(userDAO.save(user)).thenReturn(1L);
        when(userDAO.find(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                studentService.registerStudent(userDTO)
        );
        assertEquals("Не смогли найти такого пользовтеля", exception.getMessage());
    }

    @Test
    public void testUpdateStudent_Success() {
        Long studentId = 12L;
        Student existingStudent = Student.builder()
                .id(12L)
                .user(user)
                .rating(2.5)
                .build();
        Student studentUpd = Student.builder()
                .id(12L)
                .user(user)
                .rating(4.5)
                .build();
        StudentDTO studentDTOUpd = StudentDTO.builder()
                .userDTO(userDTO)
                .rating(4.5)
                .build();
        StudentDTO studentDTOResult = StudentDTO.builder()
                .userDTO(userDTO)
                .rating(4.5)
                .build();
        when(studentDAO.find(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentMapper.updateStudent(existingStudent, studentDTOUpd)).thenReturn(studentUpd);
        when(studentDAO.update(studentUpd)).thenReturn(studentUpd);
        when(studentMapper.fromStudent(studentUpd)).thenReturn(studentDTOResult);

        StudentDTO updatedStudent = studentService.updateStudent(userDetails, studentDTOUpd, studentId);

        assertNotNull(updatedStudent);
        verify(studentDAO, times(1)).update(studentUpd);
    }

    @Test
    public void testUpdateStudent_NotFound() {
        Long studentId = 1L;
        when(studentDAO.find(studentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                studentService.updateStudent(mock(org.springframework.security.core.userdetails.User.class), studentDTO, studentId)
        );
        assertEquals("Не нашли студента по id 1", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        Long studentId = 12L;
        Student student = Student.builder()
                .id(12L)
                .user(user)
                .rating(4.5)
                .build();
        StudentDTO studentDTO2 = StudentDTO.builder()
                .userDTO(userDTO)
                .rating(4.5)
                .build();
        when(studentDAO.find(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.fromStudent(any(Student.class))).thenReturn(studentDTO2);

        StudentDTO foundStudent = studentService.findById(studentId);

        assertNotNull(foundStudent);
        assertEquals("Имя студента 1", foundStudent.getUserDTO().getName());
    }

    @Test
    public void testFindById_NotFound() {
        Long studentId = 1L;
        when(studentDAO.find(studentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                studentService.findById(studentId)
        );
        assertEquals("Не нашли студента по id 1", exception.getMessage());
    }

    @Test
    public void testFindStudentsByName_Success() {
        List<Student> students = Arrays.asList(student);
        when(studentDAO.findAllByText("студ", 0, 10)).thenReturn(students);
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        List<UserDTO> studentsList = studentService.findStudentsByName("студ", 0, 10);

        assertEquals(1, studentsList.size());
        assertEquals("Имя студента 1", studentsList.get(0).getName());
    }

    @Test
    public void testFindStudentsByName_EmptyList() {
        when(studentDAO.findAllByText("НетуТакого", 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                studentService.findStudentsByName("НетуТакого", 0, 10)
        );
        assertEquals("Список пуст", exception.getMessage());
    }

    @Test
    public void testDeleteStudent_Success() {
        Long studentId = 1L;
        when(studentDAO.find(studentId)).thenReturn(Optional.of(student));

        studentService.deleteStudent(userDetails, studentId);

        verify(studentDAO, times(1)).deleteById(studentId);
    }

    @Test
    public void testDeleteStudent_NotFound() {
        Long studentId = 1L;
        when(studentDAO.find(studentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                studentService.deleteStudent(userDetails, studentId)
        );
        assertEquals("Не нашли студента по id 1", exception.getMessage());
    }

    @Test
    public void testRegisterCourseRequest_Success() {
        Long studentId = 1L;
        Long courseId = 1L;
        when(courseDAO.find(courseId)).thenReturn(Optional.of(course));
        when(studentDAO.find(studentId)).thenReturn(Optional.of(student));
        when(studentCoursesDAO.save(any(StudentsCourses.class))).thenReturn(1L);

        Long requestId = studentService.registerCourseRequest(userDetails, studentId, courseId);

        assertEquals(1L, requestId);
        verify(studentCoursesDAO, times(1)).save(any(StudentsCourses.class));
    }

    @Test
    public void testRegisterCourseRequest_CourseNotFound() {
        Long studentId = 1L;
        Long courseId = 1L;
        when(courseDAO.find(courseId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                studentService.registerCourseRequest(mock(org.springframework.security.core.userdetails.User.class), studentId, courseId)
        );
        assertEquals("Не нашли курс по id 1", exception.getMessage());
    }

    @Test
    public void testFindStudentsCoursesById_Success() {
        Long studentId = 1L;
        Long courseId = 1L;
        when(courseDAO.find(courseId)).thenReturn(Optional.of(course));
        when(studentDAO.find(studentId)).thenReturn(Optional.of(student));
        when(studentCoursesDAO.findByIds(studentId, courseId)).thenReturn(studentsCourses);
        when(studentsCoursesMapper.fromStudentCourses(studentsCourses)).thenReturn(StudentsCoursesDTO.builder().build());

        StudentsCoursesDTO courseRequest = studentService.findStudentsCoursesById(userDetails, studentId, courseId);

        assertNotNull(courseRequest);
    }

    @Test
    public void testUpdateRequest_Success() {
        List<Long> ids = Arrays.asList(1L);
        when(studentCoursesDAO.updateRequest(1L, ids, StudentsCoursesRequestEnum.APPROVED)).thenReturn(1);

        Integer affectedRows = studentService.updateRequest(1L, ids, "APPROVED");

        assertEquals(1, affectedRows);
    }

    @Test
    public void testUpdateRequest_InvalidResponse() {
        List<Long> ids = Arrays.asList(1L);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                studentService.updateRequest(1L, ids, "INVALID")
        );
        assertEquals("Передано неверное значение response -- INVALID", exception.getMessage());
    }

}
