package com.senla.courses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.all.PublicTeacherController;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = PublicTeacherController.class)
@AutoConfigureMockMvc
public class PublicTeacherControllerTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindById() throws Exception {
        Long teacherId = 1L;
        UserDTO userDTO = UserDTO.builder()
                .login("testUser2")
                .name("Ванька-Встанька")
                .email("user2@test.ru")
                .build();

        given(teacherService.registerTeacher(userDTO)).willReturn(1L);

        MvcResult result = mockMvc.perform(get("/all/teacher/{id}", teacherId))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)))
                .andReturn();
    }

    @Test
    public void testFindTeachersByName() throws Exception {
        String teacherName = "Teacher";
        List<UserDTO> teacherList = Arrays.asList(
                UserDTO.builder()
                        .login("testUser1")
                        .name("Ванька-Встанька")
                        .email("user1@test.ru")
                        .build(),
                UserDTO.builder()
                        .login("testUser2")
                        .name("Ленка-Коленка")
                        .email("user2@test.ru")
                        .build()
        );

        given(teacherService.findTeachersByName(teacherName, 1, 10)).willReturn(teacherList);

        mockMvc.perform(get("/all/teacher")
                        .param("name", teacherName)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(teacherList)));
    }

}
