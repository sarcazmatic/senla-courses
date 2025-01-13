package com.senla.courses.service;

import com.senla.courses.dao.LiteratureDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.LiteratureMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Literature;
import com.senla.courses.model.Module;
import com.senla.courses.service.literature.LiteratureServiceImpl;
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

import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
public class LiteratureServiceTest {

    @Mock
    private ModuleDAO moduleDAO;
    @Mock
    private LiteratureDAO literatureDAO;
    @Mock
    private LiteratureMapper literatureMapper;
    @InjectMocks
    private LiteratureServiceImpl literatureService;
    private LiteratureDTO literatureDTO;
    private Literature literature;
    private Module module;
    private Course course;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        course = Course.builder().id(1L).name("Курс тест 1").description("Описание курса 1").field("Поле курса 1").complexity(3).duration(6).teachers(new HashSet<>()).modules(new HashSet<>()).build();

        module = Module.builder().placeInCourse(1).description("Тест описание модуля").name("Тест название модуля").id(1L).course(course).build();

        literatureDTO = LiteratureDTO.builder().id(1L).name("Тест лит-ра").author("Тест имя автора").url("тест url").build();

        literature = Literature.builder().id(1L).name("Тест лит-ра").author("Тест имя автора").url("тест url").module(module).build();

        ReflectionTestUtils.setField(literatureService, "literatureMapper", literatureMapper);
        ReflectionTestUtils.setField(literatureService, "moduleDAO", moduleDAO);
        ReflectionTestUtils.setField(literatureService, "literatureDAO", literatureDAO);

    }

    @Test
    public void testAdd() {
        when(moduleDAO.find(1L)).thenReturn(Optional.of(module));
        when(literatureMapper.fromLiteratureDTO(any(LiteratureDTO.class))).thenReturn(literature);
        when(literatureDAO.save(any(Literature.class))).thenReturn(1L);

        Long savedId = literatureService.add(literatureDTO, 1L);

        assertEquals(1L, savedId);
        verify(literatureDAO, times(1)).save(any(Literature.class));
        verify(moduleDAO, times(1)).find(1L);
    }

    @Test
    public void testAddModuleNotFound() {
        when(moduleDAO.find(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> literatureService.add(literatureDTO, 1L));
        assertEquals("На нашли модуля по id 1", exception.getMessage());
    }

    @Test
    public void testEdit() {
        LiteratureDTO literatureDTOUpdate = LiteratureDTO.builder().build();
        literatureDTOUpdate.setName("Обновленная лит-ра");

        Literature literatureUpd = new Literature();
        literatureUpd.setName("Обновленная лит-ра");

        when(literatureDAO.find(1L)).thenReturn(Optional.of(literature));
        when(literatureMapper.updateLiterature(literature, literatureDTOUpdate)).thenReturn(literatureUpd);
        when(literatureDAO.update(literatureUpd)).thenReturn(literatureUpd);
        when(literatureMapper.fromLiterature(literatureUpd)).thenReturn(literatureDTOUpdate);

        LiteratureDTO result = literatureService.edit(literatureDTOUpdate, 1L);

        assertNotNull(result);
        assertEquals("Обновленная лит-ра", result.getName());
        verify(literatureDAO, times(1)).find(1L);
        verify(literatureMapper, times(1)).updateLiterature(literature, literatureDTOUpdate);
    }

    @Test
    public void testEditLiteratureNotFound() {
        when(literatureDAO.find(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> literatureService.edit(literatureDTO, 1L));
        assertEquals("Не удалось найти литературу", exception.getMessage());
    }

    @Test
    public void testFindById() {
        when(literatureDAO.find(1L)).thenReturn(Optional.of(literature));
        when(literatureMapper.fromLiterature(any(Literature.class))).thenReturn(literatureDTO);

        LiteratureDTO foundDTO = literatureService.findById(1L);

        assertNotNull(foundDTO);
        assertEquals(literatureDTO.getName(), foundDTO.getName());
        verify(literatureDAO, times(1)).find(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(literatureDAO.find(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> literatureService.findById(1L));
        assertEquals("Не удалось найти литературу", exception.getMessage());
    }

    @Test
    public void testDelete() {
        when(literatureDAO.find(1L)).thenReturn(Optional.of(literature));

        literatureService.delete(1L);

        verify(literatureDAO, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteLiteratureNotFound() {
        when(literatureDAO.find(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> literatureService.delete(1L));
        assertEquals("Не удалось найти литературу", exception.getMessage());
    }

}
