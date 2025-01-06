package com.senla.courses.controller.module;

import com.senla.courses.dto.FileDTO;
import com.senla.courses.service.files.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController()
@RequestMapping("/module/file")
@AllArgsConstructor
public class ModuleFileController {

    private final FileService fileService;


    @PostMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long uploadFile(@RequestParam(name = "file") MultipartFile mpFile,
                           @RequestParam(name = "url", required = false) String url,
                           @PathVariable(name = "moduleId") Long moduleId) throws IOException {
        System.out.println(mpFile.getOriginalFilename());
        return fileService.save(mpFile, url, moduleId);
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<byte[]> editFile(@RequestParam(name = "file") MultipartFile mpFile,
                                           @RequestParam(name = "url", required = false) String url,
                                           @PathVariable Long fileId) throws IOException {
        System.out.println(Arrays.toString(mpFile.getBytes()));
        FileDTO fileDTO = fileService.edit(mpFile, url, fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDTO.getContentType()))
                .body(fileDTO.getContent());
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        FileDTO fileDTO = fileService.findById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDTO.getContentType()))
                .body(fileDTO.getContent());
    }

    @DeleteMapping("/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('teacher:write')")
    public void deleteFile(@PathVariable Long fileId) {
        fileService.delete(fileId);
    }

}
