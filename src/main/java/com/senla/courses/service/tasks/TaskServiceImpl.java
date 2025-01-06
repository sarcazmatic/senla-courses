package com.senla.courses.service.tasks;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dao.TaskDAO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.TaskMapper;
import com.senla.courses.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ModuleDAO moduleDAO;
    private final TaskDAO taskDAO;
    private final TaskMapper taskMapper;

    @Override
    public Long add(TaskDTO taskDTO, Long id) {
        Task task = taskMapper.fromTaskDTO(taskDTO);
        task.setModule(moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id)));
        return taskDAO.save(task);
    }

    @Override
    public TaskDTO edit(TaskDTO taskDTO, Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        return taskMapper.fromTask(taskDAO.update(taskMapper.updateTask(task, taskDTO)));
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        return taskMapper.fromTask(task);
    }

    @Override
    public List<TaskDTO> findByText(String text, int from, int size) {
        List<Task> tasks = taskDAO.findAllByText(text, from, size);
        if (tasks.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return tasks.stream().map(taskMapper::fromTask).toList();
    }

    @Override
    public List<TaskDTO> findByModuleId(Long moduleId, int from, int size) {
        List<Task> tasks = taskDAO.findAllByModuleId(moduleId, from, size);
        if (tasks.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return tasks.stream().map(taskMapper::fromTask).toList();
    }

    @Override
    public void delete(Long id) {
        taskDAO.deleteById(id);
    }

}
