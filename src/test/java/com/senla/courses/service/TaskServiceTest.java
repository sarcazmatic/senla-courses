package com.senla.courses.service;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dao.TaskDAO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.TaskMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Module;
import com.senla.courses.model.Task;
import com.senla.courses.service.tasks.TaskServiceImpl;
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
public class TaskServiceTest {

    @Mock
    private ModuleDAO moduleDAO;
    @Mock
    private TaskDAO taskDAO;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskDTO taskDTO;
    private Task task;
    private Module module;
    private Course course;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        taskDTO = TaskDTO.builder()
                .name("Тестовая задача")
                .body("Тело тестовой задачи")
                .url("Тестовая юрлка")
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

        task = Task.builder()
                .id(1L)
                .name("Тестовая задача")
                .body("Тело тестовой задачи")
                .url("Тестовая юрлка")
                .module(module)
                .build();

        ReflectionTestUtils.setField(taskService, "taskMapper", taskMapper);
        ReflectionTestUtils.setField(taskService, "moduleDAO", moduleDAO);
        ReflectionTestUtils.setField(taskService, "taskDAO", taskDAO);
    }

    @Test
    public void testAdd_Success() {
        Long moduleId = 1L;
        when(moduleDAO.find(moduleId)).thenReturn(Optional.of(module));
        when(taskMapper.fromTaskDTO(taskDTO)).thenReturn(task);
        when(taskDAO.save(task)).thenReturn(1L);

        Long taskId = taskService.add(taskDTO, moduleId);

        assertEquals(1L, taskId);
        verify(taskDAO, times(1)).save(task);
        verify(moduleDAO, times(1)).find(moduleId);
    }

    @Test
    public void testAdd_ModuleNotFound() {
        Long moduleId = 1L;
        when(moduleDAO.find(moduleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                taskService.add(taskDTO, moduleId)
        );
        assertEquals("На нашли модуля по id 1", exception.getMessage());
    }

    @Test
    public void testEdit_Success() {
        TaskDTO taskDTOChange = TaskDTO.builder().build();
        taskDTOChange.setName("Обновленный таск");

        Task taskExist = task;
        taskExist.setName("Старый таск");

        Long taskId = 1L;

        when(taskDAO.find(taskId)).thenReturn(Optional.of(taskExist));
        when(taskMapper.updateTask(taskExist, taskDTOChange)).thenReturn(task);
        when(taskDAO.update(any(Task.class))).thenReturn(task);
        when(taskMapper.fromTask(task)).thenReturn(taskDTOChange);


        TaskDTO updatedTask = taskService.edit(taskDTOChange, taskId);

        assertNotNull(updatedTask);
        assertEquals("Обновленный таск", updatedTask.getName());
        verify(taskDAO, times(1)).update(task);
    }

    @Test
    public void testEdit_TaskNotFound() {
        Long taskId = 1L;
        when(taskDAO.find(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                taskService.edit(taskDTO, taskId)
        );
        assertEquals("Не удалось найти задачу", exception.getMessage());
    }

    @Test
    public void testFindById_Success() {
        Long taskId = 1L;
        when(taskDAO.find(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.fromTask(task)).thenReturn(taskDTO);


        TaskDTO foundTask = taskService.findById(taskId);

        assertNotNull(foundTask);
        assertEquals("Тестовая задача", foundTask.getName());
    }

    @Test
    public void testFindById_TaskNotFound() {
        Long taskId = 1L;
        when(taskDAO.find(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                taskService.findById(taskId)
        );
        assertEquals("Не удалось найти задачу", exception.getMessage());
    }

    @Test
    public void testFindByText_Success() {
        List<Task> tasks = Arrays.asList(task);
        when(taskDAO.findAllByText("тест", 0, 10)).thenReturn(tasks);
        when(taskMapper.fromTask(task)).thenReturn(taskDTO);

        List<TaskDTO> foundTasks = taskService.findByText("тест", 0, 10);

        assertNotNull(foundTasks);
        assertEquals(1, foundTasks.size());
        assertEquals("Тестовая задача", foundTasks.get(0).getName());
    }

    @Test
    public void testFindByText_EmptyList() {
        when(taskDAO.findAllByText("NonExistent", 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                taskService.findByText("NonExistent", 0, 10)
        );
        assertEquals("Список пуст", exception.getMessage());
    }

    @Test
    public void testFindByModuleId_Success() {
        Long moduleId = 1L;
        List<Task> tasks = Arrays.asList(task);
        when(taskDAO.findAllByModuleId(moduleId, 0, 10)).thenReturn(tasks);
        when(taskMapper.fromTask(task)).thenReturn(taskDTO);

        List<TaskDTO> foundTasks = taskService.findByModuleId(moduleId, 0, 10);

        assertNotNull(foundTasks);
        assertEquals(1, foundTasks.size());
        assertEquals("Тестовая задача", foundTasks.get(0).getName());
    }

    @Test
    public void testFindByModuleId_EmptyList() {
        Long moduleId = 1L;
        when(taskDAO.findAllByModuleId(moduleId, 0, 10)).thenReturn(Collections.emptyList());

        EmptyListException exception = assertThrows(EmptyListException.class, () ->
                taskService.findByModuleId(moduleId, 0, 10)
        );
        assertEquals("Список пуст", exception.getMessage());
    }

    @Test
    public void testDelete_Success() {
        Long taskId = 1L;
        when(taskDAO.find(taskId)).thenReturn(Optional.of(task));

        taskService.delete(taskId);

        verify(taskDAO, times(1)).deleteById(taskId);
    }

    @Test
    public void testDelete_TaskNotFound() {
        Long taskId = 1L;
        when(taskDAO.find(taskId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                taskService.delete(taskId)
        );
        assertEquals("Не удалось найти задачу", exception.getMessage());
    }

}
