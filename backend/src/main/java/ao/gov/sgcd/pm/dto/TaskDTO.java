package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.TaskStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;
    private Long sprintId;
    private Integer sprintNumber;
    private String sprintName;
    private String taskCode;
    private LocalDate sessionDate;
    private String dayOfWeek;
    private Integer weekNumber;
    private BigDecimal plannedHours;
    private String title;
    private String titleEn;
    private String description;
    private List<String> deliverables;
    private List<String> validationCriteria;
    private String coverageTarget;
    private TaskStatus status;
    private BigDecimal actualHours;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String completionNotes;
    private String blockers;
    private Integer sortOrder;
    private List<TaskNoteDTO> notes;
    private List<TaskExecutionDTO> executions;
}
