package com.senla.courses.service.files;

import com.senla.courses.dto.FileDTO;
import com.senla.courses.dto.ReturnFileDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    Long save(MultipartFile mpFile, String url, Long moduleId) throws IOException;

    FileDTO findById(Long fileId);

    FileDTO edit(MultipartFile mpFile, String url, Long fileId) throws IOException;

    void delete(Long fileId);

    List<ReturnFileDTO> findFilesByModuleId(Long moduleId, HttpServletRequest request);

}
