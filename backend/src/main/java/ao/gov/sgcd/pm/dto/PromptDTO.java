package ao.gov.sgcd.pm.dto;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PromptDTO {
    private Long taskId;
    private String taskCode;
    private String title;
    private String prompt;
}
