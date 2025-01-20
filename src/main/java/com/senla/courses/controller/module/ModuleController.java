package com.senla.courses.controller.module;

import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.dto.ReturnFileDTO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.service.files.FileService;
import com.senla.courses.service.literature.LiteratureService;
import com.senla.courses.service.module.ModuleService;
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
@RequestMapping("/module")
@AllArgsConstructor
@Slf4j
public class ModuleController {

    private final ModuleService moduleService;
    private final TaskService taskService;
    private final LiteratureService literatureService;
    private final FileService fileService;


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('teacher:write')")
    public Long addModule(@RequestBody @Valid ModuleDTO moduleDTO,
                          HttpServletRequest request) {
        log.info("Получен запрос на добавление модуля: {}. Эндпоинт {}. Метод {}",
                moduleDTO, request.getRequestURL(), request.getMethod());
        return moduleService.addModule(moduleDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public ModuleDTO editModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                @PathVariable Long id,
                                HttpServletRequest request) {
        log.info("Получен запрос на редактирование модуля с id: {}, новые данные: {}. Эндпоинт {}. Метод {}",
                id, moduleDTO, request.getRequestURL(), request.getMethod());
        return moduleService.editModule(moduleDTO, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO findById(@PathVariable Long id,
                              HttpServletRequest request) {
        log.info("Получен запрос на получение модуля с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        return moduleService.findModule(id);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByText(@RequestParam(required = false, name = "text", defaultValue = "") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Получен запрос на поиск модулей по описанию: '{}', параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                text, from, size, request.getRequestURL(), request.getMethod());
        return moduleService.findModulesByDesc(text, from, size);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByName(@RequestParam(required = false, name = "text", defaultValue = "") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Получен запрос на поиск модулей по имени: '{}', параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                text, from, size, request.getRequestURL(), request.getMethod());
        return moduleService.findModulesByName(text, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('admin:write')")
    public void deleteModule(@PathVariable("id") Long id,
                             HttpServletRequest request) {
        log.info("Получен запрос на удаление модуля с id: {}. Эндпоинт {}. Метод {}",
                id, request.getRequestURL(), request.getMethod());
        moduleService.deleteModule(id);
    }

    @GetMapping("/{moduleId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByModuleId(@PathVariable(name = "moduleId") Long moduleId,
                                            @RequestParam(required = false, defaultValue = "1") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info("Получен запрос на получение задач для модуля с id: {}, параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                moduleId, from, size, request.getRequestURL(), request.getMethod());
        return taskService.findByModuleId(moduleId, from, size);
    }

    @GetMapping("/{moduleId}/lit")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByModuleId(@PathVariable(name = "moduleId") Long moduleId,
                                                 @RequestParam(required = false, defaultValue = "1") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.info("Получен запрос на получение литературы для модуля с id: {}, параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                moduleId, from, size, request.getRequestURL(), request.getMethod());
        return literatureService.findByModuleId(moduleId, from, size);
    }

    @GetMapping("/{moduleId}/files")
    public List<ReturnFileDTO> findFilesByModuleId(@PathVariable Long moduleId, HttpServletRequest request) {
        log.info("Получен запрос на получение файлов для модуля с id: {}. Эндпоинт {}. Метод {}",
                moduleId, request.getRequestURL(), request.getMethod());
        return fileService.findFilesByModuleId(moduleId, request);
    }

}
