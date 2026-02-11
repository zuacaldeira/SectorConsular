package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.ReportDTO;
import ao.gov.sgcd.pm.entity.SprintReport;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "sprint.id", target = "sprintId")
    @Mapping(source = "sprint.sprintNumber", target = "sprintNumber")
    @Mapping(source = "sprint.name", target = "sprintName")
    ReportDTO toDto(SprintReport report);

    List<ReportDTO> toDtoList(List<SprintReport> reports);
}
