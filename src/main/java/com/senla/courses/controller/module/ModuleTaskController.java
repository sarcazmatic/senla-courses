package com.senla.courses.controller.module;

import com.senla.courses.dto.TaskDTO;
import com.senla.courses.service.tasks.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/module/task")
@AllArgsConstructor
@Slf4j
public class ModuleTaskController {

    private final TaskService taskService;

    @PostMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('teacher:write')")
    public Long addTask(@RequestBody @Valid TaskDTO taskDTO,
                        @PathVariable(name = "moduleId") Long moduleId,
                        HttpServletRequest request) {
        log.info("Получен запрос на добавление задачи в модуль с id: {}. Эндпоинт {}. Метод {}",
                moduleId, request.getRequestURL(), request.getMethod());
        return taskService.add(taskDTO, moduleId);
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public TaskDTO editTask(@RequestBody @Valid TaskDTO taskDTO,
                            @PathVariable Long taskId,
                            HttpServletRequest request) {
        log.info("Получен запрос на редактирование задачи с id: {}. Эндпоинт {}. Метод {}",
                taskId, request.getRequestURL(), request.getMethod());
        return taskService.edit(taskDTO, taskId);
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO findTaskById(@PathVariable Long taskId,
                                HttpServletRequest request) {
        log.info("Получен запрос на поиск задачи с id: {}. Эндпоинт {}. Метод {}",
                taskId, request.getRequestURL(), request.getMethod());
        return taskService.findById(taskId);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByText(@RequestParam(required = false, name = "text", defaultValue = "") String text,
                                        @RequestParam(required = false, defaultValue = "1") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size,
                                        HttpServletRequest request) {
        log.info("Получен запрос на поиск задач по тексту: '{}', страница: {}, размер: {}. Эндпоинт {}. Метод {}",
                text, from, size, request.getRequestURL(), request.getMethod());
        return taskService.findByText(text, from, size);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public void deleteTask(@PathVariable Long taskId,
                           HttpServletRequest request) {
        log.info("Получен запрос на удаление задачи с id: {}. Эндпоинт {}. Метод {}",
                taskId, request.getRequestURL(), request.getMethod());
        taskService.delete(taskId);
    }

}
