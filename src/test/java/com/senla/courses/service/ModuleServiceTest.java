package com.senla.courses.service;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.ModuleMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Module;
import com.senla.courses.service.module.ModuleServiceImpl;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ModuleServiceTest {

    @Mock
    private CourseDAO courseDAO;
    @Mock
    private ModuleDAO moduleDAO;
    @Mock
    private ModuleMapper moduleMapper;
    @InjectMocks
    private ModuleServiceImpl moduleService;

    private ModuleDTO moduleDTO;
    private Course course;
    private Module module;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        moduleDTO = ModuleDTO.builder()
                .description("Тест описание модуля")
                .name("Тест название модуля")
                .placeInCourse(1)
                .courseId(1L)
                .build();

        course = Course.builder()
                .id(1L)
                .name("Курс тест 1")
                .description("Описание курса 1")
                .field("Поле курса 1")
                .complexity(3)
                .duration(6)
                .teachers(new HashSet<>())
                .modules(new HashSet<>())
                .build();

        module = Module.builder()
                .placeInCourse(1)
                .description("Тест описание модуля")
                .name("Тест название модуля")
                .id(1L)
                .course(course)
                .build();

        ReflectionTestUtils.setField(moduleService, "moduleMapper", moduleMapper);
        ReflectionTestUtils.setField(moduleService, "courseDAO", courseDAO);
        ReflectionTestUtils.setField(moduleService, "moduleDAO", moduleDAO);
    }

    @Test
    public void testAddModule_Success() {
        when(courseDAO.find(moduleDTO.getCourseId())).thenReturn(Optional.of(course));
        when(moduleMapper.fromModuleDTO(moduleDTO)).thenReturn(module);
        when(moduleDAO.save(module)).thenReturn(1L);
        when(courseDAO.update(course)).thenReturn(course);

        Long moduleId = moduleService.addModule(moduleDTO);

        assertEquals(1L, moduleId);
        verify(moduleDAO, times(1)).save(module);
        verify(courseDAO, times(1)).update(course);
    }

    @Test
    public void testAddModule_CourseNotFound() {
        when(courseDAO.find(moduleDTO.getCourseId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                moduleService.addModule(moduleDTO)
        );
        assertEquals("На нашли курса по id 1", exception.getMessage());
    }

    @Test
    public void testEditModule_Success() {
        Long moduleId = 1L;
        Module existingModule = Module.builder()
                .id(moduleId)
                .name("Старое название")
                .description("Старое описание")
                .placeInCourse(2)
                .course(course)
                .build();
        ModuleDTO moduleDTOUPD = ModuleDTO.builder()
                .name("Новое название")
                .description("Новое описание")
                .build();
        when(moduleDAO.find(moduleId)).thenReturn(Optional.of(existingModule));
        when(courseDAO.find(any())).thenReturn(Optional.of(course));
        when(moduleMapper.updateModule(existingModule, moduleDTOUPD)).thenReturn(existingModule);
        when(moduleDAO.update(existingModule)).thenReturn(existingModule);
        when(moduleMapper.fromModule(any(Module.class))).thenReturn(moduleDTOUPD);

        ModuleDTO updatedModuleDTO = moduleService.editModule(moduleDTOUPD, moduleId);

        assertNotNull(updatedModuleDTO);
        assertEquals("Новое название", updatedModuleDTO.getName());
        assertEquals("Новое описание", updatedModuleDTO.getDescription());
        verify(moduleDAO, times(1)).update(existingModule);
    }

    @Test
    public void testEditModule_ModuleNotFound() {
        Long moduleId = 1L;
        when(moduleDAO.find(moduleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                moduleService.editModule(moduleDTO, moduleId)
        );
        assertEquals("На нашли модуля по id 1", exception.getMessage());
    }

    @Test
    public void testFindModule_Success() {
        Long moduleId = 2L;
        Module moduleToFind = Module.builder()
                .placeInCourse(1)
                .description("Тест описание модуля 2")
                .name("Тест название модуля 2")
                .id(2L)
                .course(course)
                .build();
        when(moduleDAO.find(moduleId)).thenReturn(Optional.of(moduleToFind));
        when(moduleMapper.fromModule(any(Module.class))).thenReturn(moduleDTO);

        ModuleDTO foundModule = moduleService.findModule(moduleId);

        assertNotNull(foundModule);
        assertEquals("Тест название модуля", foundModule.getName());
        assertEquals("Тест описание модуля", foundModule.getDescription());
    }

    @Test
    public void testFindModule_ModuleNotFound() {
        Long moduleId = 1L;
        when(moduleDAO.find(moduleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                moduleService.findModule(moduleId)
        );
        assertEquals("На нашли модуля по id 1", exception.getMessage());
    }

    @Test
    public void testFindModulesByName_Success() {
        List<Module> modules = Arrays.asList(module);
        when(moduleDAO.findAllByName("модуля", 0, 10)).thenReturn(modules);
        when(moduleMapper.fromModule(module)).thenReturn(moduleDTO);

        List<ModuleDTO> moduleDTOs = moduleService.findModulesByName("модуля", 0, 10);

        assertEquals(1, moduleDTOs.size());
        assertEquals("Тест название модуля", moduleDTOs.get(0).getName());
    }

    @Test
    public void testFindModulesByName_EmptyList() {
        when(moduleDAO.findAllByName("НетИмени", 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                moduleService.findModulesByName("НетИмени", 0, 10)
        );
        assertEquals("Список пуст", exception.getMessage());
    }

    @Test
    public void testFindModulesByDesc_Success() {
        List<Module> modules = Arrays.asList(module);
        when(moduleDAO.findAllByText("описание", 0, 10)).thenReturn(modules);
        when(moduleMapper.fromModule(module)).thenReturn(moduleDTO);

        List<ModuleDTO> moduleDTOs = moduleService.findModulesByDesc("описание", 0, 10);

        assertEquals(1, moduleDTOs.size());
        assertEquals("Тест название модуля", moduleDTOs.get(0).getName());
    }

    @Test
    public void testFindModulesByDesc_EmptyList() {
        when(moduleDAO.findAllByText("Нет описания", 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                moduleService.findModulesByDesc("Нет описания", 0, 10)
        );
        assertEquals("Список пуст", exception.getMessage());
    }

    @Test
    public void testDeleteModule_Success() {
        Long moduleId = 1L;
        when(moduleDAO.find(moduleId)).thenReturn(Optional.of(module));

        moduleService.deleteModule(moduleId);

        verify(moduleDAO, times(1)).deleteById(moduleId);
    }

    @Test
    public void testDeleteModule_ModuleNotFound() {
        Long moduleId = 1L;
        when(moduleDAO.find(moduleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                moduleService.deleteModule(moduleId)
        );
        assertEquals("На нашли модуля по id 1", exception.getMessage());
    }
}
