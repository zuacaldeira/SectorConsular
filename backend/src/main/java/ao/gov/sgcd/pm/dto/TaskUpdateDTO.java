package ao.gov.sgcd.pm.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TaskUpdateDTO {
    private String completionNotes;
    private String blockers;
    private BigDecimal actualHours;
    private String description;
}
