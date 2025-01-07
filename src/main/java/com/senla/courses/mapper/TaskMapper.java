package com.senla.courses.mapper;

import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.TaskDTO;
import com.senla.courses.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ModuleDAO moduleDAO;
    private final ModuleMapper moduleMapper;


    public Task fromTaskDTO(TaskDTO taskDTO) {
        return Task.builder()
                .body(taskDTO.getBody())
                .name(taskDTO.getName())
                .url(taskDTO.getUrl())
                .build();
    }

    public TaskDTO fromTask(Task task) {
        TaskDTO taskDTO = TaskDTO.builder()
                .id(task.getId())
                .body(task.getBody())
                .name(task.getName())
                .url(task.getUrl())
                .build();

            taskDTO.setModule(moduleMapper.fromModule(task.getModule()));

        return taskDTO;
    }

    public Task updateTask(Task task, TaskDTO taskDTO) {
        if (taskDTO.getBody() != null) {
            task.setBody(taskDTO.getBody());
        }
        if (taskDTO.getName() != null) {
            task.setName(taskDTO.getName());
        }
        if (taskDTO.getUrl() != null) {
            task.setUrl(taskDTO.getUrl());
        }
        if (taskDTO.getModule() != null) {
            task.setModule(moduleMapper.fromModuleDTO(taskDTO.getModule()));
        }
        return task;
    }

}
