package com.senla.courses.service.files;

import com.senla.courses.dao.FileDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.FileDTO;
import com.senla.courses.dto.ReturnFileDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.FileMapper;
import com.senla.courses.model.File;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDAO fileDAO;
    private final ModuleDAO moduleDAO;
    private final FileMapper fileMapper;

    @Override
    public Long save(MultipartFile mpFile, String url, Long moduleId) throws IOException {
        File file = fileMapper.fromMPFile(mpFile, url);
        file.setModule(moduleDAO.find(moduleId).orElseThrow(()
                -> new NotFoundException("Не нашли модуля для прикрепления файла")));
        return fileDAO.save(file);
    }

    @Override
    public FileDTO findById(Long fileId) {
         File file = fileDAO.find(fileId).orElseThrow(()
                -> new NotFoundException("Не нашли файл с id " + fileId));
        return fileMapper.fromFile(file);
    }

    @Override
    public FileDTO edit(MultipartFile mpFile, String url, Long fileId) throws IOException {
        File file = fileDAO.find(fileId).orElseThrow(()
                -> new NotFoundException("Не нашли файл с id " + fileId));
        File fileOut = fileMapper.updateFile(file, mpFile, url);
        return fileMapper.fromFile(fileDAO.update(fileOut));
    }

    @Override
    public void delete(Long fileId) {
        fileDAO.deleteById(fileId);
    }

    @Override
    public List<ReturnFileDTO> findFilesByModuleId(Long moduleId, HttpServletRequest request) {
        String path = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI()));
        List<File> files = fileDAO.findAllByModuleId(moduleId);
        if (files.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return files.stream().map(f -> fileMapper.fromFileToReturn(f, path)).toList();
    }

}
