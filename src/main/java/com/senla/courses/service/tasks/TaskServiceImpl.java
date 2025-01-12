package com.senla.courses.service.tasks;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dao.TaskDAO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.TaskMapper;
import com.senla.courses.model.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        Long idReturn = taskDAO.save(task);
        log.info("Задание {} зарегестрировано под id {}", task, idReturn);
        return idReturn;
    }

    @Override
    public TaskDTO edit(TaskDTO taskDTO, Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        Task taskResult = taskDAO.update(taskMapper.updateTask(task, taskDTO));
        log.info("Задание с id {} отредактировано. Было: {}. Стало: {}", id, task, taskResult);
        return taskMapper.fromTask(taskResult);
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        log.info("Задание {} с id {} найдено", task, id);
        return taskMapper.fromTask(task);
    }

    @Override
    public List<TaskDTO> findByText(String text, int from, int size) {
        List<Task> tasks = taskDAO.findAllByText(text, from, size);
        if (tasks.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<TaskDTO> taskDTOS = tasks.stream().map(taskMapper::fromTask).toList();
        log.info("Собран список задач с текстом {}. Найдено {} элементов", text, taskDTOS.size());
        return taskDTOS;
    }

    @Override
    public List<TaskDTO> findByModuleId(Long moduleId, int from, int size) {
        List<Task> tasks = taskDAO.findAllByModuleId(moduleId, from, size);
        if (tasks.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<TaskDTO> taskDTOS = tasks.stream().map(taskMapper::fromTask).toList();
        log.info("Собран список задач модуля с id {}. Найдено {} элементов", moduleId, taskDTOS.size());
        return taskDTOS;
    }

    @Override
    public void delete(Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        taskDAO.deleteById(id);
        log.info("Задание {} с id {} удалено", task, id);
    }

}
