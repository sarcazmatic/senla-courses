package com.senla.courses.controller.admin;

import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.service.module.ModuleService;
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

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Long addModule(@RequestBody @Valid ModuleDTO moduleDTO) {
        return moduleService.addModule(moduleDTO);
    }

    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO editModule(@RequestBody @Valid ModuleDTO moduleDTO) {
        return moduleService.editModule(moduleDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO findModule(@PathVariable Long id) {
        return moduleService.findModule(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findModules(@RequestParam(required = false, name = "text") String text,
                                       @RequestParam (required = false, defaultValue = "1") int from,
                                       @RequestParam (required = false, defaultValue = "10") int size) {
        return moduleService.findModules(text, from, size);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteModule(@PathVariable("id") Long id) {
        moduleService.deleteModule(id);
    }

}
