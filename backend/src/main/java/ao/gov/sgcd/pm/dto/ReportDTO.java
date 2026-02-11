package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.ReportType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReportDTO {
    private Long id;
    private Long sprintId;
    private Integer sprintNumber;
    private String sprintName;
    private ReportType reportType;
    private Integer weekNumber;
    private LocalDateTime generatedAt;
    private String summaryPt;
    private String summaryEn;
    private String metricsJson;
    private String pdfPath;
}
