package com.senla.courses.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.teacher.TeacherController;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Mock
    private User userSec;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("admin", "admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

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

    @Test
    @PreAuthorize("hasAuthority('admin:write')")
    public void testUpdateTeacher() throws Exception {
        Long teacherId = 1L;
        UserDTO userDTO = UserDTO.builder()
                .name("Валя")
                .login("tea")
                .email("valya@thebest.com")
                .password("{noop}tea")
                .age(25)
                .description("Let's go!")
                .role("TEACHER")
                .build();

        when(authentication.getPrincipal()).thenReturn("admin:write");
        when(userSec.getUsername()).thenReturn("tea");
        when(userSec.getPassword()).thenReturn("tea");
        when(userSec.getAuthorities()).thenReturn(Set.of(new SimpleGrantedAuthority("teacher:write")));
        given(teacherService.updateTeacher(userSec, userDTO, teacherId)).willReturn(userDTO);

        mockMvc.perform(put("/teacher/{id}", teacherId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

}
