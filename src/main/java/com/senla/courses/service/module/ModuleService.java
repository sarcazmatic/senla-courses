package com.senla.courses.service.module;

import com.senla.courses.dto.course.CourseDTO;
import com.senla.courses.dto.module.ModuleDTO;
import com.senla.courses.model.Module;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ModuleService {

    Long addModule(ModuleDTO moduleDTO);

    ModuleDTO editModule(ModuleDTO moduleDTO);

    ModuleDTO findModule(Long id);

    List<ModuleDTO> findModules(String text, int from, int size);

    void deleteModule(Long id);

}
