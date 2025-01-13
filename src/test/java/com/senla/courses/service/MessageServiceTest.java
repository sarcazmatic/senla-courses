package com.senla.courses.service;

import com.senla.courses.dao.MessageDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.MessageMapper;
import com.senla.courses.model.Message;
import com.senla.courses.model.Role;
import com.senla.courses.model.Student;
import com.senla.courses.model.Teacher;
import com.senla.courses.model.User;
import com.senla.courses.service.messages.MessageServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MessageServiceTest {

    @Mock
    private MessageMapper messageMapper;
    @Mock
    private StudentDAO studentDAO;
    @Mock
    private TeacherDAO teacherDAO;
    @Mock
    private MessageDAO messageDAO;
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private MessageServiceImpl messageService;

    private MessageDTO messageDTO;
    private User userTeacher;
    private User userStudent;
    private Student student;
    private Teacher teacher;
    private Message message;
    private org.springframework.security.core.userdetails.User userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userTeacher = User.builder()
                .age(100)
                .id(10L)
                .description("Тестовое описание юзера 1")
                .email("user@test.ru")
                .password("{noop}123321")
                .login("testUser1")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.TEACHER)
                .build();

        userDetails = new org.springframework.security.core.userdetails.User(userTeacher.getLogin(),
                userTeacher.getPassword(),
                userTeacher.getRole().getAuthorities());

        userStudent = User.builder()
                .age(25)
                .id(12L)
                .description("Тестовое описание юзера 2")
                .email("user2@test.ru")
                .password("{noop}123321")
                .login("testUser2")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.STUDENT)
                .build();

        teacher = Teacher.builder()
                .user(userTeacher)
                .id(userTeacher.getId())
                .build();

        student = Student.builder()
                .user(userStudent)
                .id(userStudent.getId())
                .rating(0.0)
                .build();

        message = Message.builder()
                .id(1L)
                .from(userTeacher)
                .to(userStudent)
                .body("Тестовое сообщение")
                .build();

        messageDTO = MessageDTO.builder()
                .dateTimeSent(LocalDateTime.now())
                .teacher(teacher)
                .student(student)
                .body("Тестовое сообщение")
                .build();

        ReflectionTestUtils.setField(messageService, "messageMapper", messageMapper);
        ReflectionTestUtils.setField(messageService, "studentDAO", studentDAO);
        ReflectionTestUtils.setField(messageService, "teacherDAO", teacherDAO);
        ReflectionTestUtils.setField(messageService, "messageDAO", messageDAO);
        ReflectionTestUtils.setField(messageService, "userDAO", userDAO);

    }

    @Test
    public void testSendMessage_Success_StudentToTeacher() {
        when(userDAO.findByLogin(userTeacher.getLogin())).thenReturn(Optional.of(userTeacher));
        when(userDAO.findByLogin(userStudent.getLogin())).thenReturn(Optional.of(userStudent));
        when(teacherDAO.find(teacher.getId())).thenReturn(Optional.of(teacher));
        when(studentDAO.find(student.getId())).thenReturn(Optional.of(student));
        when(messageMapper.fromMessageDTO(messageDTO)).thenReturn(message);
        when(messageDAO.save(message)).thenReturn(1L);

        Long messageId = messageService.sendMessage(messageDTO, userDetails, student.getId());

        assertEquals(1L, messageId);
        verify(messageDAO, times(1)).save(message);
        verify(messageMapper, times(1)).fromMessageDTO(messageDTO);
        verify(studentDAO, times(1)).find(userStudent.getId());
        verify(teacherDAO, times(1)).find(userTeacher.getId());
    }

    @Test
    public void testSendMessage_MessageNotFound_NotFoundException() {
        Long toUserId = student.getId();
        when(userDAO.findByLogin(userTeacher.getLogin())).thenReturn(Optional.of(userTeacher));
        when(studentDAO.find(student.getId())).thenReturn(Optional.of(student));
        when(studentDAO.find(toUserId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                messageService.sendMessage(messageDTO, userDetails, toUserId)
        );
        assertEquals("Не смогли найти студента ни по from, не по to", exception.getMessage());
    }

    @Test
    public void testUpdateMessage_Success() {
        Long messageId = 1L;
        Message messageToUpd = Message.builder()
                .id(1L)
                .from(userTeacher)
                .to(userStudent)
                .body("Сообщение для обновления")
                .dateTimeSent(message.getDateTimeSent())
                .build();
        MessageDTO messageDTOUpd = MessageDTO.builder()
                .body("Обновленное сообщение")
                .build();
        MessageFullDTO messageUpdFullDTO = MessageFullDTO.builder()
                .from(userTeacher.getName())
                .to(userStudent.getName())
                .body("Обновленное сообщение")
                .dateTimeSent(message.getDateTimeSent())
                .build();
        when(messageDAO.find(messageId)).thenReturn(Optional.of(messageToUpd));
        when(userDAO.findByLogin(userTeacher.getLogin())).thenReturn(Optional.of(userTeacher));
        when(messageDAO.update(messageToUpd)).thenReturn(messageToUpd);
        when(messageMapper.fromMessage(messageToUpd)).thenReturn(messageUpdFullDTO);

        MessageFullDTO updatedMessage = messageService.updateMessage(userDetails, messageDTOUpd, messageId);

        assertNotNull(updatedMessage);
        assertEquals("Обновленное сообщение", updatedMessage.getBody());
        verify(messageDAO, times(1)).update(messageToUpd);
    }

    @Test
    public void testUpdateMessage_MessageNotFound_NotFoundException() {
        Long messageId = 1L;
        when(messageDAO.find(messageId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                messageService.updateMessage(userDetails, messageDTO, messageId)
        );
        assertEquals("Не смогли найти сообщение для обновления", exception.getMessage());
    }

    @Test
    public void testDeleteMessage_Success() {
        Long messageId = 1L;
        when(messageDAO.find(messageId)).thenReturn(Optional.of(message));
        when(userDAO.findByLogin(userTeacher.getLogin())).thenReturn(Optional.of(userTeacher));

        messageService.deleteMessage(userDetails, messageId);

        verify(messageDAO, times(1)).deleteById(messageId);
    }

    @Test
    public void testDeleteMessage_MessageNotFound_NotFoundException() {
        Long messageId = 1L;
        when(messageDAO.find(messageId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                messageService.deleteMessage(userDetails, messageId)
        );
        assertEquals("Не смогли найти сообщение", exception.getMessage());
    }

    @Test
    public void testGetMessagesBetween_Success() {
        List<Long> betweenIds = Arrays.asList(userTeacher.getId(), userStudent.getId());
        List<Message> messages = Arrays.asList(message);
        when(userDAO.findByLogin(userTeacher.getLogin())).thenReturn(Optional.of(userTeacher));
        when(messageDAO.findMessagesBetween(betweenIds.get(0), betweenIds.get(1), 0, 10)).thenReturn(messages);

        List<MessageFullDTO> messageFullDTOList = messageService.getMessagesBetween(userDetails, betweenIds, 0, 10);

        assertFalse(messageFullDTOList.isEmpty());
        verify(messageDAO, times(1)).findMessagesBetween(betweenIds.get(0), betweenIds.get(1), 0, 10);
    }

    @Test
    public void testGetMessagesBetween_EmptyListException() {
        List<Long> betweenIds = Arrays.asList(userTeacher.getId(), userStudent.getId());
        when(userDAO.findByLogin(userTeacher.getLogin())).thenReturn(Optional.of(userTeacher));
        when(messageDAO.findMessagesBetween(betweenIds.get(0), betweenIds.get(1), 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                messageService.getMessagesBetween(userDetails, betweenIds, 0, 10)
        );
        assertEquals("Список сообщений между указанными id пуст", exception.getMessage());
    }

}
