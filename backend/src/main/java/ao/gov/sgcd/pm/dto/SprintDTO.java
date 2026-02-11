package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.SprintStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SprintDTO {
    private Long id;
    private Integer sprintNumber;
    private String name;
    private String nameEn;
    private String description;
    private Integer weeks;
    private Integer totalHours;
    private Integer totalSessions;
    private LocalDate startDate;
    private LocalDate endDate;
    private String focus;
    private String color;
    private SprintStatus status;
    private BigDecimal actualHours;
    private Integer completedSessions;
    private String completionNotes;
    private Integer taskCount;
    private Double progressPercent;
}
