package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.task.TaskCreateDTO;
import io.hexlet.spring.dto.task.TaskDTO;
import io.hexlet.spring.dto.task.TaskUpdateDTO;
import io.hexlet.spring.model.Task;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract Task map(TaskCreateDTO dto);
    @Mapping(source = "assignee.id", target = "assigneeId")
    public abstract TaskDTO map(Task model);

    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
}
