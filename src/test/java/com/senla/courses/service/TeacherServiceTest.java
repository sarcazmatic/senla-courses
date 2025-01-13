package com.senla.courses.service;

import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.model.Role;
import com.senla.courses.model.Teacher;
import com.senla.courses.model.User;
import com.senla.courses.service.teachers.TeacherServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
public class TeacherServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private TeacherDAO teacherDAO;
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private TeacherServiceImpl teacherService;

    private UserDTO userDTO;
    private Teacher teacher;
    private User user;
    private User userToUpd;
    private org.springframework.security.core.userdetails.User userDetails;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = UserDTO.builder()
                .login("testUser")
                .name("Имя преподавателя")
                .email("user2@test.ru")
                .build();

        user = User.builder()
                .age(25)
                .id(12L)
                .name("Имя преподавателя")
                .description("Тестовое описание юзера")
                .email("user2@test.ru")
                .password("{noop}123321")
                .login("testUser")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.TEACHER)
                .build();

        userToUpd = User.builder()
                .age(23)
                .id(12L)
                .description("Тестовое описание юзера для обновления")
                .email("user2@test.ru")
                .password("{noop}123321")
                .login("testUser")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.TEACHER)
                .build();

        teacher = Teacher.builder()
                .id(1L)
                .user(user)
                .courses(new HashSet<>())
                .build();

        userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                user.getRole().getAuthorities());

        ReflectionTestUtils.setField(teacherService, "teacherDAO", teacherDAO);
        ReflectionTestUtils.setField(teacherService, "userMapper", userMapper);
        ReflectionTestUtils.setField(teacherService, "userDAO", userDAO);
    }

    @Test
    public void testRegisterTeacher_Success() {
        when(userMapper.fromUserDTO(userDTO)).thenReturn(user);
        when(userDAO.save(user)).thenReturn(1L);
        when(userDAO.find(1L)).thenReturn(Optional.of(user));
        when(teacherDAO.save(any(Teacher.class))).thenReturn(1L);

        Long teacherId = teacherService.registerTeacher(userDTO);

        assertEquals(1L, teacherId);
        verify(userDAO, times(1)).save(user);
        verify(teacherDAO, times(1)).save(any(Teacher.class));
    }

    @Test
    public void testRegisterTeacher_UserNotFound() {
        when(userMapper.fromUserDTO(userDTO)).thenReturn(user);
        when(userDAO.save(user)).thenReturn(1L);
        when(userDAO.find(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                teacherService.registerTeacher(userDTO)
        );
        assertEquals("Не смогли найти такого пользовтеля", exception.getMessage());
    }

    @Test
    public void testUpdateTeacher_Success() {
        Teacher teacherExist = Teacher.builder()
                .id(1L)
                .user(userToUpd)
                .courses(new HashSet<>())
                .build();
        UserDTO userDTOResult = UserDTO.builder()
                .name("Имя преподавателя")
                .build();
        User user = User.builder()
                .age(25)
                .id(12L)
                .name("Имя преподавателя")
                .description("Тестовое описание юзера 2")
                .email("user2@test.ru")
                .password("{noop}123321")
                .login("testUser")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.TEACHER)
                .build();
        Teacher teacherResult = Teacher.builder()
                .id(1L)
                .user(user)
                .courses(new HashSet<>())
                .build();
        Long teacherId = 1L;
        when(teacherDAO.find(teacherId)).thenReturn(Optional.of(teacherExist));
        when(userMapper.updateUser(teacherExist.getUser(), userDTOResult)).thenReturn(user);
        when(teacherDAO.update(teacherExist)).thenReturn(teacherResult);
        when(userMapper.fromUser(teacherResult.getUser())).thenReturn(userDTOResult);

        UserDTO updatedUser = teacherService.updateTeacher(userDetails, userDTOResult, teacherId);

        assertNotNull(updatedUser);
        assertEquals("Имя преподавателя", updatedUser.getName());
        verify(teacherDAO, times(1)).update(teacherExist);
    }

    @Test
    public void testUpdateTeacher_TeacherNotFound() {
        Long teacherId = 1L;
        when(teacherDAO.find(teacherId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                teacherService.updateTeacher(userDetails, userDTO, teacherId)
        );
        assertEquals("Не смогли найти учителя по id 1", exception.getMessage());
    }

    @Test
    public void testUpdateTeacher_ValidationFailed() {
        Long teacherId = 1L;
        when(teacherDAO.find(teacherId)).thenReturn(Optional.of(teacher));
        when(userMapper.updateUser(user, userDTO)).thenReturn(user);

        org.springframework.security.core.userdetails.User userSec = new org.springframework.security.core.userdetails.User("teee", "asdasd", Collections.emptyList());


        ValidationException exception = assertThrows(ValidationException.class, () ->
                teacherService.updateTeacher(userSec, userDTO, teacherId)
        );
        assertEquals("Обновить можно только инфомрацию о себе", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        Long teacherId = 1L;
        Teacher teacher = Teacher.builder()
                .id(1L)
                .user(user)
                .courses(new HashSet<>())
                .build();
        UserDTO userDTO = UserDTO.builder()
                .login("testUser2")
                .name("Имя ппкаодвывтеля для теста")
                .email("user2@test.ru")
                .build();
        when(teacherDAO.find(teacherId)).thenReturn(Optional.of(teacher));
        when(userMapper.fromUser(teacher.getUser())).thenReturn(userDTO);


        UserDTO foundTeacher = teacherService.findById(teacherId);

        assertNotNull(foundTeacher);
        assertEquals("Имя ппкаодвывтеля для теста", foundTeacher.getName());
    }

    @Test
    public void testFindById_TeacherNotFound() {
        Long teacherId = 1L;
        when(teacherDAO.find(teacherId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                teacherService.findById(teacherId)
        );
        assertEquals("Не смогли найти учителя по id 1", exception.getMessage());
    }

    @Test
    public void testFindTeachersByName_Success() {
        String name = "Имя";
        List<Teacher> teachers = Arrays.asList(teacher);
        when(teacherDAO.findAllByText(name, 0, 10)).thenReturn(teachers);
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        List<UserDTO> foundTeachers = teacherService.findTeachersByName(name, 0, 10);

        assertNotNull(foundTeachers);
        assertEquals(1, foundTeachers.size());
        assertEquals("Имя преподавателя", foundTeachers.get(0).getName());
    }

    @Test
    public void testFindTeachersByName_EmptyList() {
        String name = "НетТаких";
        when(teacherDAO.findAllByText(name, 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                teacherService.findTeachersByName(name, 0, 10)
        );
        assertEquals("Список учителей пуст", exception.getMessage());
    }

    @Test
    public void testDeleteTeacher_Success() {
        Long teacherId = 1L;
        when(teacherDAO.find(teacherId)).thenReturn(Optional.of(teacher));

        teacherService.deleteTeacher(userDetails, teacherId);

        verify(teacherDAO, times(1)).deleteById(teacherId);
    }

    @Test
    public void testDeleteTeacher_TeacherNotFound() {
        Long teacherId = 1L;
        when(teacherDAO.find(teacherId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                teacherService.deleteTeacher(userDetails, teacherId)
        );
        assertEquals("Не смогли найти учителя по id 1", exception.getMessage());
    }

}

