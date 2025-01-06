package com.senla.courses.service.tasks;

import com.senla.courses.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    Long add(TaskDTO taskDTO, Long id);

    TaskDTO edit(TaskDTO taskDTO, Long id);

    TaskDTO findById(Long id);

    List<TaskDTO> findByText(String text, int from, int size);

    List<TaskDTO> findByModuleId(Long moduleId, int from, int size);

    void delete(Long id);

}
