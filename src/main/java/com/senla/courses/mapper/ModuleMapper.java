package com.senla.courses.mapper;

import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.model.Module;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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


    public Module updateModule(Module module, ModuleDTO moduleDTO) {
        if (moduleDTO.getPlaceInCourse() != null) {
            module.setPlaceInCourse(moduleDTO.getPlaceInCourse());
        }
        if (moduleDTO.getDescription() != null) {
            module.setDescription(moduleDTO.getDescription());
        }
        if (moduleDTO.getName() != null) {
            module.setName(moduleDTO.getName());
        }
        return module;
    }

}
