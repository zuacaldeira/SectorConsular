package ao.gov.sgcd.pm.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CalendarDTO {
    private Integer year;
    private Integer month;
    private List<CalendarDayDTO> days;

    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class CalendarDayDTO {
        private LocalDate date;
        private String dayOfWeek;
        private boolean isBlocked;
        private String blockReason;
        private TaskDTO task;
        private boolean isWorkDay;
    }
}
