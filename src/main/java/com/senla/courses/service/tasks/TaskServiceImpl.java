package com.senla.courses.service.tasks;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dao.TaskDAO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.TaskMapper;
import com.senla.courses.model.Task;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final ModuleDAO moduleDAO;
    private final TaskDAO taskDAO;
    private final TaskMapper taskMapper;

    @Override
    public Long add(TaskDTO taskDTO, Long id) {
        Task task = taskMapper.fromTaskDTO(taskDTO);
        task.setModule(moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id)));
        Long idReturn = taskDAO.save(task);
        logger.info("Задание зарегестрировано под id {}", idReturn);
        return idReturn;
    }

    @Override
    public TaskDTO edit(TaskDTO taskDTO, Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        Task taskResult = taskDAO.update(taskMapper.updateTask(task, taskDTO));
        logger.info("Задание с id {} отредактировано", id);
        return taskMapper.fromTask(taskResult);
    }

    @Override
    public TaskDTO findById(Long id) {
        Task task = taskDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти задачу"));
        logger.info("Задание с id {} найдено", id);
        return taskMapper.fromTask(task);
    }

    @Override
    public List<TaskDTO> findByText(String text, int from, int size) {
        List<Task> tasks = taskDAO.findAllByText(text, from, size);
        if (tasks.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<TaskDTO> taskDTOS = tasks.stream().map(taskMapper::fromTask).toList();
        logger.info("Собран список задач с текстом {}", text);
        return taskDTOS;
    }

    @Override
    public List<TaskDTO> findByModuleId(Long moduleId, int from, int size) {
        List<Task> tasks = taskDAO.findAllByModuleId(moduleId, from, size);
        if (tasks.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<TaskDTO> taskDTOS = tasks.stream().map(taskMapper::fromTask).toList();
        logger.info("Собран список задач модуля с id {}", moduleId);
        return taskDTOS;
    }

    @Override
    public void delete(Long id) {
        taskDAO.deleteById(id);
        logger.info("Задание с id {} удалено", id);
    }

}
