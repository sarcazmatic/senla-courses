package com.senla.courses.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.message.MessageController;
import com.senla.courses.controller.teacher.TeacherController;
import com.senla.courses.dto.MessageDTO;
import com.senla.courses.dto.MessageFullDTO;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.model.Message;
import com.senla.courses.model.Role;
import com.senla.courses.model.Student;
import com.senla.courses.model.Teacher;
import com.senla.courses.service.messages.MessageService;
import com.senla.courses.service.teachers.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessageController.class)
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MessageDTO messageDTO;
    private MessageFullDTO messageFullDTO;

    private com.senla.courses.model.User userTeacher;
    private com.senla.courses.model.User userStudent;
    private Student student;
    private Teacher teacher;
    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        userTeacher = com.senla.courses.model.User.builder()
                .age(100)
                .id(10L)
                .description("Тестовое описание юзера 1")
                .email("user@test.ru")
                .password("{noop}admin")
                .login("admin")
                .role(Role.TEACHER)
                .build();

        userStudent = com.senla.courses.model.User.builder()
                .age(25)
                .id(12L)
                .description("Тестовое описание юзера 2")
                .email("user2@test.ru")
                .password("{noop}admin")
                .login("admin")
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

        messageFullDTO = MessageFullDTO.builder()
                .from(userTeacher.getName())
                .to(userStudent.getName())
                .body("Тестовое сообщение")
                .build();

        messageDTO = MessageDTO.builder()
                .teacher(teacher)
                .student(student)
                .body("Тестовое сообщение")
                .build();

    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write, teacher:write, student:write")
    void testSendMessage() throws Exception {
        Long recipientId = 2L;

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        given(messageService.sendMessage(any(MessageDTO.class), eq(user), eq(recipientId))).willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/message/send/{to}", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.disable(MapperFeature.USE_ANNOTATIONS).writeValueAsString(messageDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> result.equals(1L));

        verify(messageService, times(1)).sendMessage(any(MessageDTO.class), any(User.class), eq(recipientId));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write")
    void testEditMessage() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long messageId = 1L;
        when(messageService.updateMessage(eq(user), eq(messageDTO), eq(messageId))).thenReturn(messageFullDTO);
        given(messageService.sendMessage(messageDTO, user, 2L)).willReturn(messageId);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/message/{id}/edit", messageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> result.equals(messageFullDTO));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write")
    void testGetMessagesBetween() throws Exception {
        List<Long> betweenIds = Arrays.asList(1L, 2L);
        List<MessageFullDTO> messageList = Arrays.asList(messageFullDTO);
        when(messageService.getMessagesBetween(any(User.class), eq(betweenIds), eq(1), eq(10))).thenReturn(messageList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/message/between")
                        .param("ids", "1,2")
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(messageService, times(1)).getMessagesBetween(any(User.class), eq(betweenIds), eq(1), eq(10));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write")
    void testGetMessagesByText() throws Exception {
        String searchText = "test message";
        List<MessageFullDTO> messageList = Arrays.asList(messageFullDTO);
        given(messageService.findMessagesByText(any(User.class), eq(searchText), eq(1), eq(10))).willReturn(messageList);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/message")
                        .param("text", searchText)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(messageService, times(1)).findMessagesByText(any(User.class), eq(searchText), eq(1), eq(10));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write")
    void testDeleteMessage() throws Exception {
        Long messageId = 1L;
        doNothing().when(messageService).deleteMessage(any(User.class), eq(messageId));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/message/{id}", messageId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(messageService, times(1)).deleteMessage(any(User.class), eq(messageId));
    }
}
