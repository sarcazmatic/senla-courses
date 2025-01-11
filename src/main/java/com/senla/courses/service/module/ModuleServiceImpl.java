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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

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
        logger.info("Модуль с id {} для курса {} создан", pk, course.getName());
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
        logger.info("Модуль с id {} отредактирован", id);
        return moduleMapper.fromModule(module);
    }

    public ModuleDTO findModule(Long id) {
        Module module = moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id));
        logger.info("Модуль с id {} найден", id);
        return moduleMapper.fromModule(module);
    }

    @Override
    public List<ModuleDTO> findModulesByName(String text, int from, int size) {
        List<Module> modules = moduleDAO.findAllByName(text, from, size);
        if (modules.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<ModuleDTO> moduleDTOS = modules.stream().map(moduleMapper::fromModule).toList();
        logger.info("Собран список модулей с названием {}", text);
        return moduleDTOS;
    }

    @Override
    public List<ModuleDTO> findModulesByDesc(String text, int from, int size) {
        List<Module> modules = moduleDAO.findAllByText(text, from, size);
        if (modules.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<ModuleDTO> moduleDTOS =  modules.stream().map(moduleMapper::fromModule).toList();
        logger.info("Собран список модулей с описанием {}", text);
        return moduleDTOS;
    }

    @Override
    public void deleteModule(Long id) {
        moduleDAO.deleteById(id);
        logger.info("Модуль с id {} удален", id);

    }

}
