package com.senla.courses.service.module;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dao.TaskDAO;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.ModuleMapper;
import com.senla.courses.mapper.TaskMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Module;
import com.senla.courses.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (!courseModules.isEmpty()) {
            newCourseModules.addAll(courseModules);
        }
        course.setModules(newCourseModules);
        courseDAO.update(course);
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
        return moduleMapper.fromModule(module);
    }

    public ModuleDTO findModule(Long id) {
        Module module = moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id));
        return moduleMapper.fromModule(module);
    }

    @Override
    public List<ModuleDTO> findModules(String text, int from, int size) {
        List<Module> modules = moduleDAO.findAllByText(text, from, size);
        if (modules.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return modules.stream().map(moduleMapper::fromModule).toList();
    }

    @Override
    public void deleteModule(Long id) {
        moduleDAO.deleteById(id);
    }

}
