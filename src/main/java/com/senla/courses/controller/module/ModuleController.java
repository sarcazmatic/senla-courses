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
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestHandler;
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
public class ModuleController {

    private final ModuleService moduleService;
    private final TaskService taskService;
    private final LiteratureService literatureService;
    private final FileService fileService;


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('teacher:write')")
    public Long addModule(@RequestBody @Valid ModuleDTO moduleDTO) {
        return moduleService.addModule(moduleDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public ModuleDTO editModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                @PathVariable Long id) {
        return moduleService.editModule(moduleDTO, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO findById(@PathVariable Long id) {
        return moduleService.findModule(id);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByText(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return moduleService.findModulesByDesc(text, from, size);

    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByName(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return moduleService.findModulesByName(text, from, size);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('admin:write')")
    public void deleteModule(@PathVariable("id") Long id) {
        moduleService.deleteModule(id);
    }

    @GetMapping("/{moduleId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByModuleId(@PathVariable(name = "moduleId") Long moduleId,
                                            @RequestParam(required = false, defaultValue = "1") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return taskService.findByModuleId(moduleId, from, size);
    }

    @GetMapping("/{moduleId}/lit")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByModuleId(@PathVariable(name = "moduleId") Long moduleId,
                                                 @RequestParam(required = false, defaultValue = "1") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        return literatureService.findByModuleId(moduleId, from, size);
    }

    @GetMapping("/{moduleId}/files")
    public List<ReturnFileDTO> findFilesByModuleId(@PathVariable Long moduleId, HttpServletRequest request) {
        return fileService.findFilesByModuleId(moduleId, request);
    }

}
