package com.senla.courses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.all.PublicCourseController;
import com.senla.courses.dto.CourseDTO;
import com.senla.courses.service.courses.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = PublicCourseController.class)
@AutoConfigureMockMvc
public class PublicCourseControllerTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

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
        Long courseId = 1L;
        CourseDTO courseDTO = CourseDTO.builder()
                .description("asdas")
                .complexity(2)
                .name("xcvcxv")
                .field("dfxdvxfgf")
                .duration(3)
                .build();
        when(courseService.findById(courseId)).thenReturn(courseDTO);

        mockMvc.perform(get("/all/course/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseDTO)))
                .andReturn();
    }

    @Test
    public void testFindCourses() throws Exception {
        List<CourseDTO> courseList = Arrays.asList(
                CourseDTO.builder()
                        .description("asdas")
                        .complexity(2)
                        .name("xcvcxv")
                        .field("dfxdvxfgf")
                        .duration(3)
                        .build(),
                CourseDTO.builder()
                        .description("aasdsdas")
                        .complexity(22)
                        .name("xcvcxzxvcv")
                        .field("dfxaadvxfgf")
                        .duration(43)
                        .build()
        );
        when(courseService.findCourses(1, 10)).thenReturn(courseList);

        mockMvc.perform(get("/all/course/all")
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseList)));
    }

    @Test
    public void testFindCoursesByText() throws Exception {
        String searchText = "Java";
        List<CourseDTO> courseList = Arrays.asList(
                CourseDTO.builder()
                        .description("asdas")
                        .complexity(2)
                        .name("xcvcxv")
                        .field("dfxdvxfgf")
                        .duration(3)
                        .build(),
                CourseDTO.builder()
                        .description("aasdsdas")
                        .complexity(22)
                        .name("xcvcxzxvcv")
                        .field("dfxaadvxfgf")
                        .duration(43)
                        .build()
        );
        when(courseService.findCoursesByText(searchText, 1, 10)).thenReturn(courseList);

        mockMvc.perform(get("/all/course")
                        .param("text", searchText)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(courseList)));
    }
}