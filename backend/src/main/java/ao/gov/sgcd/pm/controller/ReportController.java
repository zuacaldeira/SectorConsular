package ao.gov.sgcd.pm.controller;

import ao.gov.sgcd.pm.dto.ReportDTO;
import ao.gov.sgcd.pm.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> findAll() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<ReportDTO> findBySprintId(@PathVariable Long sprintId) {
        return ResponseEntity.ok(reportService.findBySprintId(sprintId));
    }

    @PostMapping("/sprint/{sprintId}/generate")
    public ResponseEntity<ReportDTO> generateReport(@PathVariable Long sprintId) {
        return ResponseEntity.ok(reportService.generateReport(sprintId));
    }
}
