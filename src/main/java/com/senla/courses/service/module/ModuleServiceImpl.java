package com.senla.courses.service.module;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.ModuleMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Module;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final CourseDAO courseDAO;
    private final ModuleDAO moduleDAO;
    private final ModuleMapper moduleMapper;


    public Long addModule(ModuleDTO moduleDTO) {
        Module module = moduleMapper.fromModuleDTO(moduleDTO);
        Course course = courseDAO.find(moduleDTO.getCourseId()).orElseThrow(()
                -> new NotFoundException("На нашли курса по id " + moduleDTO.getCourseId()));
        module.setCourse(course);
        Long pk = moduleDAO.save(module);
        Set<Module> courseModules;
        try {
            courseModules = course.getModules();
        } catch (Exception e) {
            courseModules = new HashSet<>();
        }
        Set<Module> newCourseModules = new HashSet<>();
        newCourseModules.add(module);
        try {
            newCourseModules.addAll(courseModules);
        } catch (NullPointerException ignore) {
        }
        course.setModules(newCourseModules);
        courseDAO.update(course);
        log.info("Модуль {} с id {} для курса {} создан", module.getName(), pk, course.getName());
        return pk;
    }

    @Override
    public ModuleDTO editModule(ModuleDTO moduleDTO, Long id) {
        Module moduleIn = moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + moduleDTO.getCourseId()));
        Course course = courseDAO.find(moduleDTO.getCourseId()).orElseThrow(()
                -> new NotFoundException("На нашли курса по id " + moduleDTO.getCourseId()));
        moduleIn.setCourse(course);
        Module moduleOut = moduleMapper.updateModule(moduleIn, moduleDTO);
        Module module = moduleDAO.update(moduleOut);
        log.info("Модуль с id {} отредактирован. Было: {}. Стало {}.", id, moduleIn, moduleOut);
        return moduleMapper.fromModule(module);
    }

    public ModuleDTO findModule(Long id) {
        Module module = moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id));
        log.info("Модуль {} с id {} найден", module, id);
        return moduleMapper.fromModule(module);
    }

    @Override
    public List<ModuleDTO> findModulesByName(String text, int from, int size) {
        List<Module> modules = moduleDAO.findAllByName(text, from, size);
        if (modules.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<ModuleDTO> moduleDTOS = modules.stream().map(moduleMapper::fromModule).toList();
        log.info("Собран список модулей с названием {}. Найдено {} элементов", text, moduleDTOS.size());
        return moduleDTOS;
    }

    @Override
    public List<ModuleDTO> findModulesByDesc(String text, int from, int size) {
        List<Module> modules = moduleDAO.findAllByText(text, from, size);
        if (modules.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<ModuleDTO> moduleDTOS =  modules.stream().map(moduleMapper::fromModule).toList();
        log.info("Собран список модулей с описанием {}. Найдено {} элементов", text, moduleDTOS.size());
        return moduleDTOS;
    }

    @Override
    public void deleteModule(Long id) {
        Module module = moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id));
        moduleDAO.deleteById(id);
        log.info("Модуль {} с id {} удален", module, id);

    }

}
