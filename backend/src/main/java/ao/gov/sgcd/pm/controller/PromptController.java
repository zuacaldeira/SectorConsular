package ao.gov.sgcd.pm.controller;

import ao.gov.sgcd.pm.dto.PromptDTO;
import ao.gov.sgcd.pm.service.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/prompts")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    @GetMapping("/today")
    public ResponseEntity<PromptDTO> getTodayPrompt() {
        return ResponseEntity.ok(promptService.getTodayPrompt());
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<PromptDTO> getTaskPrompt(@PathVariable Long taskId) {
        return ResponseEntity.ok(promptService.getPromptForTask(taskId));
    }

    @GetMapping("/context")
    public ResponseEntity<String> getProjectContext() {
        return ResponseEntity.ok(promptService.getProjectContext());
    }
}
