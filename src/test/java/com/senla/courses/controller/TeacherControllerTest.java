package com.senla.courses.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.teacher.TeacherController;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.service.teachers.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write")
    void testUpdateTeacher() throws Exception {
        Long teacherId = 1L;
        UserDTO updatedUserDTO = UserDTO.builder()
                .name("Валя")
                .login("admin")
                .email("valya@thebest.com")
                .password("{noop}admin")
                .age(25)
                .description("Let's go!")
                .role("TEACHER")
                .build();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        given(teacherService.updateTeacher(eq(user), eq(updatedUserDTO), eq(teacherId))).willReturn(updatedUserDTO);

        mockMvc.perform(put("/teacher/{id}", teacherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> result.equals(updatedUserDTO));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "admin:write")
    void testDeleteTeacher() throws Exception {
        Long teacherId = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/teacher/{id}", teacherId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(teacherService, times(1)).deleteTeacher(any(), eq(teacherId));
    }

}
