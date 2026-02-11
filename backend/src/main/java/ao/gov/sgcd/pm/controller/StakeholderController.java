package ao.gov.sgcd.pm.controller;

import ao.gov.sgcd.pm.dto.StakeholderDashboardDTO;
import ao.gov.sgcd.pm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/stakeholder")
@RequiredArgsConstructor
public class StakeholderController {

    private final DashboardService dashboardService;

    @Value("${sgcd-pm.stakeholder.token}")
    private String validToken;

    @GetMapping
    public ResponseEntity<StakeholderDashboardDTO> getDashboard(
            @RequestParam(required = false) String token) {
        if (token != null && !token.equals(validToken)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(dashboardService.getStakeholderDashboard());
    }
}
