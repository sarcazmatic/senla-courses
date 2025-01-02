package com.senla.courses.mapper;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.FileDTO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.model.File;
import com.senla.courses.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileMapper {

    private final ModuleDAO moduleDAO;
    private final ModuleMapper moduleMapper;


    public File fromFileDTO(FileDTO fileDTO) {
        return File.builder()
                .name(fileDTO.getName())
                .content(fileDTO.getContent())
                .url(fileDTO.getUrl())
                .contentType(fileDTO.getContentType())
                .build();
    }

    public FileDTO fromFile(File file) {
        FileDTO fileDTO = FileDTO.builder()
                .content(file.getContent())
                .name(file.getName())
                .url(file.getUrl())
                .contentType(file.getContentType())
                .build();

        fileDTO.setModule(moduleMapper.fromModule(file.getModule()));

        return fileDTO;
    }

    public File updateFile(File file, FileDTO fileDTO) {
        if (fileDTO.getContent() != null) {
            file.setContent(fileDTO.getContent());
        }
        if (fileDTO.getName() != null) {
            file.setName(fileDTO.getName());
        }
        if (fileDTO.getUrl() != null) {
            file.setUrl(fileDTO.getUrl());
        }
        if (fileDTO.getContentType() != null) {
            file.setContentType(fileDTO.getContentType());
        }
        if (fileDTO.getModule() != null) {
            file.setModule(moduleMapper.fromModuleDTO(fileDTO.getModule()));
        }

        return file;
    }

}
