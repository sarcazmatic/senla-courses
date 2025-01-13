package com.senla.courses.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.all.PublicTeacherController;
import com.senla.courses.controller.teacher.TeacherController;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.model.Role;
import com.senla.courses.service.teachers.TeacherService;
import com.senla.courses.util.UserDetailsExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = TeacherController.class)
@AutoConfigureMockMvc
public class TeacherControllerTest {

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
    public void testRegisterTeacher() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .name("Валя")
                .login("admin")
                .email("valya@thebest.com")
                .password("{noop}admin")
                .age(25)
                .description("Let's go!")
                .role("TEACHER")
                .build();
        Long teacherId = 1L;

        given(teacherService.registerTeacher(userDTO)).willReturn(teacherId);

        mockMvc.perform(post("/teacher/register")
                        .contentType("application/json")
                        .content(objectMapper.disable(MapperFeature.USE_ANNOTATIONS).writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(result -> result.equals(1L));
    }

}
