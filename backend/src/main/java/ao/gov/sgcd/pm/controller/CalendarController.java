package ao.gov.sgcd.pm.controller;

import ao.gov.sgcd.pm.dto.BlockedDayDTO;
import ao.gov.sgcd.pm.dto.CalendarDTO;
import ao.gov.sgcd.pm.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<CalendarDTO> getCalendar(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();
        return ResponseEntity.ok(calendarService.getCalendar(y, m));
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<BlockedDayDTO>> getBlockedDays() {
        return ResponseEntity.ok(calendarService.getBlockedDays());
    }
}
