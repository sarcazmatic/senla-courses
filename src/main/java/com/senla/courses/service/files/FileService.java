package com.senla.courses.service.files;

import com.senla.courses.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    Long save(MultipartFile mpFile, String url, Long moduleId) throws IOException;

    FileDTO findById(Long fileId);

    FileDTO edit(MultipartFile mpFile, String url, Long fileId) throws IOException;

    void delete(Long fileId);

}
