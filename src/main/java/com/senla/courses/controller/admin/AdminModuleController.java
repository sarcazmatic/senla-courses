package com.senla.courses.controller.admin;

import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.service.literature.LiteratureService;
import com.senla.courses.service.module.ModuleService;
import com.senla.courses.service.tasks.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/admin/module")
@AllArgsConstructor
public class AdminModuleController {

    private final ModuleService moduleService;
    private final TaskService taskService;
    private final LiteratureService literatureService;


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addModule(@RequestBody @Valid ModuleDTO moduleDTO) {
        return moduleService.addModule(moduleDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO editModule(@RequestBody @Valid ModuleDTO moduleDTO,
                                @PathVariable Long id) {
        return moduleService.editModule(moduleDTO, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO findById(@PathVariable Long id) {
        return moduleService.findModule(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByText(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        return moduleService.findModules(text, from, size);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteModule(@PathVariable("id") Long id) {
        moduleService.deleteModule(id);
    }

    //ниже таски

    @PostMapping("/{id}/task")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable(name = "id") Long moduleId) {
        return taskService.add(taskDTO, moduleId);
    }

    @PutMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO editTask(@RequestBody @Valid TaskDTO taskDTO,
                         @PathVariable Long taskId) {
        return taskService.edit(taskDTO, taskId);
    }

    @GetMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO findTaskById(@PathVariable Long taskId) {
        return taskService.findById(taskId);
    }

    @GetMapping("/task")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByText(@RequestParam(required = false, name = "text") String text,
                                        @RequestParam(required = false, defaultValue = "1") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return taskService.findByText(text, from, size);
    }

    @GetMapping("/{id}/task")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findTaskByModuleId(@PathVariable(name = "id") Long moduleId,
                                        @RequestParam(required = false, defaultValue = "1") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return taskService.findByModuleId(moduleId, from, size);
    }

    @DeleteMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
    }

    //ниже литература

    @PostMapping("/{id}/lit")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addLit(@RequestBody @Valid LiteratureDTO literatureDTO, @PathVariable(name = "id") Long moduleId) {
        return literatureService.add(literatureDTO, moduleId);
    }

    @PutMapping("/lit/{litId}")
    @ResponseStatus(HttpStatus.OK)
    public LiteratureDTO editLit(@RequestBody @Valid LiteratureDTO literatureDTO,
                            @PathVariable Long litId) {
        return literatureService.edit(literatureDTO, litId);
    }

    @GetMapping("/lit/{litId}")
    @ResponseStatus(HttpStatus.OK)
    public LiteratureDTO findLitById(@PathVariable Long litId) {
        return literatureService.findById(litId);
    }

    @GetMapping("/lit")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByText(@RequestParam(required = false, name = "text") String text,
                                        @RequestParam(required = false, defaultValue = "1") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return literatureService.findByText(text, from, size);
    }

    @GetMapping("/{id}/lit")
    @ResponseStatus(HttpStatus.OK)
    public List<LiteratureDTO> findLitByModuleId(@PathVariable(name = "id") Long moduleId,
                                        @RequestParam(required = false, defaultValue = "1") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return literatureService.findByModuleId(moduleId, from, size);
    }

    @DeleteMapping("/lit/{litId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLit(@PathVariable Long litId) {
        literatureService.delete(litId);
    }

}
