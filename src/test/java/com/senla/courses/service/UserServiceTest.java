package com.senla.courses.service;


import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.model.Role;
import com.senla.courses.model.User;
import com.senla.courses.service.users.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDAO userDao;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = UserDTO.builder()
                .login("testUser2")
                .name("Ванька-Встанька")
                .email("user2@test.ru")
                .build();

        user = User.builder()
                .age(25)
                .id(12L)
                .description("Тестовое описание юзера 2")
                .email("user2@test.ru")
                .password("{noop}123321")
                .login("testUser2")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.TEACHER)
                .build();

        ReflectionTestUtils.setField(userService, "userMapper", userMapper);
        ReflectionTestUtils.setField(userService, "userDao", userDao);
    }

    @Test
    public void testFindById_Success() {
        Long userId = 19L;
        when(userDao.find(userId)).thenReturn(Optional.of(user));
        when(userMapper.fromUser(user)).thenReturn(userDTO);

        UserDTO foundUser = userService.findById(userId);

        assertNotNull(foundUser);
        assertEquals("Ванька-Встанька", foundUser.getName());
        assertEquals("testUser2", foundUser.getLogin());
        verify(userDao, times(1)).find(userId);
        verify(userMapper, times(1)).fromUser(user);
    }

    @Test
    public void testFindById_UserNotFound() {
        Long userId = 1L;
        when(userDao.find(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                userService.findById(userId)
        );
        assertEquals("Не нашли пользователя по id 1", exception.getMessage());
        verify(userDao, times(1)).find(userId);
    }
}

