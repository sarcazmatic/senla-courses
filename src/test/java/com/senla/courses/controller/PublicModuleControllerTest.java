package com.senla.courses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.courses.controller.all.PublicModuleController;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.service.module.ModuleServiceImpl;
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


@WebMvcTest(controllers = PublicModuleController.class)
@AutoConfigureMockMvc
public class PublicModuleControllerTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @MockBean
    private ModuleServiceImpl moduleService;

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
        Long moduleId = 1L;
        ModuleDTO moduleDTO = ModuleDTO.builder()
                .description("Тест описание модуля")
                .name("Тест название модуля")
                .placeInCourse(1)
                .courseId(1L)
                .build();

        given(moduleService.findModule(moduleId)).willReturn(moduleDTO);

        mockMvc.perform(get("/all/module/{id}", moduleId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(moduleDTO)))
                .andReturn();
    }

    @Test
    public void testFindByText() throws Exception {
        String searchText = "Java";
        List<ModuleDTO> moduleList = Arrays.asList(
                ModuleDTO.builder()
                        .description("Тест описание модуля")
                        .name("Тест название модуля")
                        .placeInCourse(1)
                        .courseId(1L)
                        .build(),
                ModuleDTO.builder()
                        .description("Тест описание модуля 2")
                        .name("Тест название модуля 2")
                        .placeInCourse(2)
                        .courseId(1L)
                        .build()
        );

        given(moduleService.findModulesByDesc(searchText, 1, 10)).willReturn(moduleList);

        mockMvc.perform(get("/all/module/find")
                        .param("text", searchText)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(moduleList)));
    }

    @Test
    public void testFindByName() throws Exception {
        String searchText = "Java";
        List<ModuleDTO> moduleList = Arrays.asList(
                ModuleDTO.builder()
                        .description("Тест описание модуля")
                        .name("Тест название модуля")
                        .placeInCourse(1)
                        .courseId(1L)
                        .build(),
                ModuleDTO.builder()
                        .description("Тест описание модуля 2")
                        .name("Тест название модуля 2")
                        .placeInCourse(2)
                        .courseId(1L)
                        .build()
        );

        given(moduleService.findModulesByName(searchText, 1, 10)).willReturn(moduleList);

        mockMvc.perform(get("/all/module/name")
                        .param("text", searchText)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(moduleList)));
    }

}
