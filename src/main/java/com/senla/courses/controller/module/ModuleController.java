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
    public Long addModule(@RequestBody @Valid ModuleDTO moduleDTO) {
        log.info("Получен запрос на добавление модуля: {}", moduleDTO);
        Long id = moduleService.addModule(moduleDTO);
        log.info("Модуль успешно добавлен с id: {}", id);
        return id;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public ModuleDTO editModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                @PathVariable Long id) {
        log.info("Получен запрос на редактирование модуля с id: {}, новые данные: {}", id, moduleDTO);
        ModuleDTO moduleDTOUpd = moduleService.editModule(moduleDTO, id);
        log.info("Модуль с id: {} успешно отредактирован", id);
        return moduleDTOUpd;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO findById(@PathVariable Long id) {
        log.info("Получен запрос на получение модуля с id: {}", id);
        ModuleDTO moduleDTO = moduleService.findModule(id);
        log.info("Модуль с id: {} успешно найден", id);
        return moduleDTO;
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByText(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск модулей по описанию: '{}', параметры: from={}, size={}", text, from, size);
        List<ModuleDTO> moduleDTOS = moduleService.findModulesByDesc(text, from, size);
        log.info("Модули успешно найдены по описанию: '{}', параметры: from={}, size={}", text, from, size);
        return moduleDTOS;
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByName(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на поиск модулей по имени: '{}', параметры: from={}, size={}", text, from, size);
        List<ModuleDTO> moduleDTOS = moduleService.findModulesByName(text, from, size);
        log.info("Модули успешно найдены по названию: '{}', параметры: from={}, size={}", text, from, size);
        return moduleDTOS;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('admin:write')")
    public void deleteModule(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление модуля с id: {}", id);
        moduleService.deleteModule(id);
        log.info("Модуль с id: {} успешно удалён", id);
    }

    @GetMapping("/{moduleId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByModuleId(@PathVariable(name = "moduleId") Long moduleId,
                                            @RequestParam(required = false, defaultValue = "1") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на получение задач для модуля с id: {}, параметры: from={}, size={}", moduleId, from, size);
        List<TaskDTO> taskDTOS = taskService.findByModuleId(moduleId, from, size);
        log.info("Задачи для модуля с id: {} успешно найдены, параметры: from={}, size={}", moduleId, from, size);
        return taskDTOS;
    }

    @GetMapping("/{moduleId}/lit")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByModuleId(@PathVariable(name = "moduleId") Long moduleId,
                                                 @RequestParam(required = false, defaultValue = "1") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос на получение литературы для модуля с id: {}, параметры: from={}, size={}", moduleId, from, size);
        List<LiteratureDTO> literatureDTOS = literatureService.findByModuleId(moduleId, from, size);
        log.info("Литература для модуля с id: {} успешно найдена, параметры: from={}, size={}", moduleId, from, size);
        return literatureDTOS;
    }

    @GetMapping("/{moduleId}/files")
    public List<ReturnFileDTO> findFilesByModuleId(@PathVariable Long moduleId, HttpServletRequest request) {
        log.info("Получен запрос на получение файлов для модуля с id: {}", moduleId);
        List<ReturnFileDTO> fileDTOS = fileService.findFilesByModuleId(moduleId, request);
        log.info("Файлы для модуля с id: {} успешно найдены", moduleId);
        return fileDTOS;
    }

}
