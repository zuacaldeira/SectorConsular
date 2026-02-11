package ao.gov.sgcd.pm.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SprintProgressDTO {
    private Integer sprintNumber;
    private String name;
    private Integer totalSessions;
    private Integer completedSessions;
    private Integer totalHours;
    private BigDecimal actualHours;
    private Double progressPercent;
    private Integer plannedTasks;
    private Integer inProgressTasks;
    private Integer completedTasks;
    private Integer blockedTasks;
    private Integer skippedTasks;
}
