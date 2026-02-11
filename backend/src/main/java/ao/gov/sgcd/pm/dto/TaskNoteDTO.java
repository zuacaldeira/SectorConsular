package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.NoteType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TaskNoteDTO {
    private Long id;
    private Long taskId;
    private NoteType noteType;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
