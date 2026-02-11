package ao.gov.sgcd.pm.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TaskExecutionDTO {
    private Long id;
    private Long taskId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private BigDecimal hoursSpent;
    private String promptUsed;
    private String responseSummary;
    private String notes;
}
