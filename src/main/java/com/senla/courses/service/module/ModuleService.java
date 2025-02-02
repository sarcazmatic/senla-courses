package com.senla.courses.service.module;

import com.senla.courses.dto.ModuleDTO;

import java.util.List;

public interface ModuleService {

    Long addModule(ModuleDTO moduleDTO);

    ModuleDTO editModule(ModuleDTO moduleDTO, Long id);

    ModuleDTO findModule(Long id);

    List<ModuleDTO> findModulesByDesc(String text, int from, int size);

    List<ModuleDTO> findModulesByName(String text, int from, int size);


    void deleteModule(Long id);

}
