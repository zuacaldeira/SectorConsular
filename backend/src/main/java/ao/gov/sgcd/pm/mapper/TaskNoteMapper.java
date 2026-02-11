package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.TaskNoteDTO;
import ao.gov.sgcd.pm.entity.TaskNote;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskNoteMapper {

    @Mapping(source = "task.id", target = "taskId")
    TaskNoteDTO toDto(TaskNote note);

    List<TaskNoteDTO> toDtoList(List<TaskNote> notes);
}
