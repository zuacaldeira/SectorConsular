package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.TaskExecutionDTO;
import ao.gov.sgcd.pm.entity.TaskExecution;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskExecutionMapper {

    @Mapping(source = "task.id", target = "taskId")
    TaskExecutionDTO toDto(TaskExecution execution);

    List<TaskExecutionDTO> toDtoList(List<TaskExecution> executions);
}
