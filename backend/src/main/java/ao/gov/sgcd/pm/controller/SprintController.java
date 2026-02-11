package ao.gov.sgcd.pm.controller;

import ao.gov.sgcd.pm.dto.SprintDTO;
import ao.gov.sgcd.pm.dto.SprintProgressDTO;
import ao.gov.sgcd.pm.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @GetMapping
    public ResponseEntity<List<SprintDTO>> findAll() {
        return ResponseEntity.ok(sprintService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sprintService.findById(id));
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<SprintProgressDTO> getProgress(@PathVariable Long id) {
        return ResponseEntity.ok(sprintService.getProgress(id));
    }

    @GetMapping("/active")
    public ResponseEntity<SprintDTO> findActive() {
        return ResponseEntity.ok(sprintService.findActive());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SprintDTO> update(@PathVariable Long id, @RequestBody SprintDTO dto) {
        return ResponseEntity.ok(sprintService.update(id, dto));
    }
}
