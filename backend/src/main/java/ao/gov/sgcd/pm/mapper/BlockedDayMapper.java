package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.BlockedDayDTO;
import ao.gov.sgcd.pm.entity.BlockedDay;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlockedDayMapper {

    BlockedDayDTO toDto(BlockedDay blockedDay);

    List<BlockedDayDTO> toDtoList(List<BlockedDay> blockedDays);
}
