package com.senla.courses.controller.module;

import com.senla.courses.dto.TaskDTO;
import com.senla.courses.service.tasks.TaskService;
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
    public Long addTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable(name = "moduleId") Long moduleId) {
        log.info("Получен запрос на добавление задачи в модуль с id: {}", moduleId);
        Long id = taskService.add(taskDTO, moduleId);
        log.info("Задача успешно добавлена в модуль с id: {}, id задачи: {}", moduleId, id);
        return id;
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public TaskDTO editTask(@RequestBody @Valid TaskDTO taskDTO,
                            @PathVariable Long taskId) {
        log.info("Получен запрос на редактирование задачи с id: {}", taskId);
        TaskDTO taskDTOUpd = taskService.edit(taskDTO, taskId);
        log.info("Задача с id: {} успешно отредактирована", taskId);
        return taskDTOUpd;
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO findTaskById(@PathVariable Long taskId) {
        log.info("Получен запрос на поиск задачи с id: {}", taskId);
        TaskDTO taskDTO = taskService.findById(taskId);
        log.info("Задача с id: {} успешно найдена", taskId);
        return taskDTO;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByText(@RequestParam(required = false, name = "text") String text,
                                        @RequestParam(required = false, defaultValue = "1") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск задач по тексту: '{}', страница: {}, размер: {}", text, from, size);
        List<TaskDTO> taskDTOS = taskService.findByText(text, from, size);
        log.info("Найдено {} задач по тексту '{}'", taskDTOS.size(), text);
        return taskDTOS;
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public void deleteTask(@PathVariable Long taskId) {
        log.info("Получен запрос на удаление задачи с id: {}", taskId);
        taskService.delete(taskId);
        log.info("Задача с id: {} успешно удалена", taskId);
    }

}
