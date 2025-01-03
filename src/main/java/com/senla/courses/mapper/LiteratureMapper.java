package com.senla.courses.mapper;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.model.Literature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LiteratureMapper {

    private final ModuleMapper moduleMapper;


    public Literature fromLiteratureDTO(LiteratureDTO literatureDTO) {
        return Literature.builder()
                .author(literatureDTO.getAuthor())
                .name(literatureDTO.getName())
                .url(literatureDTO.getUrl())
                .build();
    }

    public LiteratureDTO fromLiterature(Literature literature) {
        LiteratureDTO literatureDTO = LiteratureDTO.builder()
                .author(literature.getAuthor())
                .name(literature.getName())
                .url(literature.getUrl())
                .build();

        literatureDTO.setModule(moduleMapper.fromModule(literature.getModule()));

        return literatureDTO;
    }

    public Literature updateLiterature(Literature literature, LiteratureDTO literatureDTO) {
        if (literatureDTO.getAuthor() != null) {
            literature.setAuthor(literatureDTO.getAuthor());
        }
        if (literatureDTO.getName() != null) {
            literature.setName(literatureDTO.getName());
        }
        if (literatureDTO.getUrl() != null) {
            literature.setUrl(literatureDTO.getUrl());
        }
        if (literatureDTO.getModule() != null) {
            literature.setModule(moduleMapper.fromModuleDTO(literatureDTO.getModule()));
        }
        return literature;
    }

}
