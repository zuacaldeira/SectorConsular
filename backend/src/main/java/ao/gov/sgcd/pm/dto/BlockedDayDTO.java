package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.BlockType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BlockedDayDTO {
    private Long id;
    private LocalDate blockedDate;
    private String dayOfWeek;
    private BlockType blockType;
    private String reason;
    private BigDecimal hoursLost;
}
