package com.senla.courses.dto.module;

import com.senla.courses.model.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public Module fromModuleDTO(ModuleDTO moduleDTO) {
        return Module.builder()
                .description(moduleDTO.getDescription())
                .placeInCourse(moduleDTO.getPlaceInCourse())
                .name(moduleDTO.getName())
                .build();
    }

    public ModuleDTO fromModule(Module module) {
        return ModuleDTO.builder()
                .description(module.getDescription())
                .placeInCourse(module.getPlaceInCourse())
                .name(module.getName())
                .courseName(module.getCourse().getName())
                .name(module.getName())
                .build();
    }

}
