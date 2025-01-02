package com.senla.courses.service.module;

import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.dto.TaskDTO;

import java.util.List;

public interface ModuleService {

    Long addModule(ModuleDTO moduleDTO);

    ModuleDTO editModule(ModuleDTO moduleDTO, Long id);

    ModuleDTO findModule(Long id);

    List<ModuleDTO> findModules(String text, int from, int size);

    void deleteModule(Long id);

}
