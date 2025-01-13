package com.senla.courses.controller.all;

import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.service.module.ModuleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/all/module")
@AllArgsConstructor
@Slf4j
public class PublicModuleController {

    private final ModuleService moduleService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDTO findById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос на поиск модуля с id: {}. Эндпоинт {}. Метод {}", id, request.getRequestURL(), request.getMethod());
        return moduleService.findModule(id);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByText(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Получен запрос на поиск модулей по описанию: '{}', параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                text, from, size, request.getRequestURL(), request.getMethod());
        return moduleService.findModulesByDesc(text, from, size);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<ModuleDTO> findByName(@RequestParam(required = false, name = "text") String text,
                                      @RequestParam(required = false, defaultValue = "1") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Получен запрос на поиск модулей по названию: '{}', параметры: from={}, size={}. Эндпоинт {}. Метод {}",
                text, from, size, request.getRequestURL(), request.getMethod());
        return moduleService.findModulesByName(text, from, size);
    }

}
