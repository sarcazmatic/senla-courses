package com.senla.courses.mapper;

import com.senla.courses.dto.FileDTO;
import com.senla.courses.dto.ReturnFileDTO;
import com.senla.courses.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileMapper {

    private final ModuleMapper moduleMapper;
    private static final String FILES_LOC = "/module/file/";


    public File fromMPFile(MultipartFile mpFile, String url) throws IOException {
        return File.builder()
                .name(mpFile.getOriginalFilename())
                .content(mpFile.getBytes())
                .url(url)
                .contentType(mpFile.getContentType())
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

    public ReturnFileDTO fromFileToReturn(File file, String path) {
        return ReturnFileDTO.builder()
                .name(file.getName())
                .endpoint(path + FILES_LOC + file.getId())
                .build();
    }

    public File updateFile(File file, MultipartFile mpfile, String url) throws IOException {
        file.setContent(mpfile.getBytes());
        file.setName(mpfile.getOriginalFilename());
        if (url != null) {
            file.setUrl(url);
        }
        if (mpfile.getContentType() != null) {
            file.setContentType(mpfile.getContentType());
        }

        return file;
    }

}
