package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.TaskDTO;
import ao.gov.sgcd.pm.entity.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {TaskNoteMapper.class, TaskExecutionMapper.class})
public interface TaskMapper {

    @Mapping(source = "sprint.id", target = "sprintId")
    @Mapping(source = "sprint.sprintNumber", target = "sprintNumber")
    @Mapping(source = "sprint.name", target = "sprintName")
    @Mapping(target = "deliverables", expression = "java(jsonToList(task.getDeliverables()))")
    @Mapping(target = "validationCriteria", expression = "java(jsonToList(task.getValidationCriteria()))")
    TaskDTO toDto(Task task);

    List<TaskDTO> toDtoList(List<Task> tasks);

    default List<String> jsonToList(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}
