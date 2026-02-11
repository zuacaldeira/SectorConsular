package ao.gov.sgcd.pm.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardDTO {
    private Double projectProgress;
    private Integer totalSessions;
    private Integer completedSessions;
    private Integer totalHoursPlanned;
    private BigDecimal totalHoursSpent;
    private SprintDTO activeSprint;
    private TaskDTO todayTask;
    private List<TaskDTO> recentTasks;
    private List<SprintSummaryDTO> sprintSummaries;
    private List<BlockedDayDTO> upcomingBlockedDays;
    private WeekProgressDTO weekProgress;

    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class SprintSummaryDTO {
        private Integer sprintNumber;
        private String name;
        private Double progress;
        private String status;
        private String color;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class WeekProgressDTO {
        private Integer weekTasks;
        private Integer weekCompleted;
        private BigDecimal weekHoursPlanned;
        private BigDecimal weekHoursSpent;
    }
}
