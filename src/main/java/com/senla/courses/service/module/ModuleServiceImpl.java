package com.senla.courses.service.module;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.ModuleDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.ModuleMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Module;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return moduleDAO.save(module);
    }

    @Override
    public ModuleDTO editModule(ModuleDTO moduleDTO) {
        Module moduleIn = moduleMapper.fromModuleDTO(moduleDTO);
        Course course = courseDAO.find(moduleDTO.getCourseId()).orElseThrow(()
                -> new NotFoundException("На нашли курса по id " + moduleDTO.getCourseId()));
        moduleIn.setCourse(course);
        Module moduleOut =  moduleDAO.update(moduleIn);
        return moduleMapper.fromModule(moduleOut);
    }

    public ModuleDTO findModule(Long id) {
        Module module = moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id));
        return moduleMapper.fromModule(module);
    }

    @Override
    public List<ModuleDTO> findModules(String text, int from, int size) {
        List<Module> modules = moduleDAO.findAllByText(text, from, size);
        return modules.stream().map(moduleMapper::fromModule).toList();
    }

    @Override
    public void deleteModule(Long id) {
        moduleDAO.deleteById(id);
    }


}
