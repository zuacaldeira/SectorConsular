package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.SprintDTO;
import ao.gov.sgcd.pm.entity.Sprint;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SprintMapper {

    @Mapping(target = "taskCount", ignore = true)
    @Mapping(target = "progressPercent", ignore = true)
    SprintDTO toDto(Sprint sprint);

    List<SprintDTO> toDtoList(List<Sprint> sprints);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Sprint toEntity(SprintDTO dto);
}
