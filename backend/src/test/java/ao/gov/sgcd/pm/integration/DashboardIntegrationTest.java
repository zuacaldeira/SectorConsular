package ao.gov.sgcd.pm.integration;

import ao.gov.sgcd.pm.dto.DashboardDTO;
import ao.gov.sgcd.pm.dto.ProjectProgressDTO;
import ao.gov.sgcd.pm.dto.StakeholderDashboardDTO;
import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskStatus;
import ao.gov.sgcd.pm.repository.SprintRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import ao.gov.sgcd.pm.seed.DataSeeder;
import ao.gov.sgcd.pm.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DashboardIntegrationTest {

    @MockitoBean
    private DataSeeder dataSeeder;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        sprintRepository.deleteAll();

        Sprint sprint = sprintRepository.save(Sprint.builder()
                .sprintNumber(1)
                .name("Sprint 1 — Fundação")
                .nameEn("Sprint 1 — Foundation")
                .description("Base do projecto")
                .weeks(10)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 5, 8))
                .focus("Infraestrutura")
                .color("#CC092F")
                .status(SprintStatus.ACTIVE)
                .build());

        taskRepository.save(Task.builder()
                .sprint(sprint)
                .taskCode("S1-01")
                .sessionDate(LocalDate.of(2026, 3, 2))
                .dayOfWeek("Seg")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(4))
                .title("Setup inicial do projecto")
                .deliverables("[\"Setup Docker\"]")
                .validationCriteria("[\"Docker funcional\"]")
                .coverageTarget("N/A")
                .status(TaskStatus.PLANNED)
                .sortOrder(1)
                .build());
    }

    @Test
    void getDeveloperDashboard_shouldReturnValidData() {
        DashboardDTO dashboard = dashboardService.getDeveloperDashboard();

        assertThat(dashboard).isNotNull();
        assertThat(dashboard.getTotalSessions()).isGreaterThan(0);
        assertThat(dashboard.getProjectProgress()).isBetween(0.0, 100.0);
        assertThat(dashboard.getSprintSummaries()).isNotEmpty();
        assertThat(dashboard.getWeekProgress()).isNotNull();
    }

    @Test
    void getProjectProgress_shouldReturnValidData() {
        ProjectProgressDTO progress = dashboardService.getProjectProgress();

        assertThat(progress).isNotNull();
        assertThat(progress.getTotalSessions()).isGreaterThan(0);
        assertThat(progress.getTotalHoursPlanned()).isGreaterThan(0);
        assertThat(progress.getOverallProgress()).isBetween(0.0, 100.0);
        assertThat(progress.getSprints()).isNotEmpty();
    }

    @Test
    void getStakeholderDashboard_shouldReturnValidData() {
        StakeholderDashboardDTO stakeholder = dashboardService.getStakeholderDashboard();

        assertThat(stakeholder).isNotNull();
        assertThat(stakeholder.getProjectName()).isNotBlank();
        assertThat(stakeholder.getClient()).isNotBlank();
        assertThat(stakeholder.getTotalSessions()).isGreaterThan(0);
        assertThat(stakeholder.getSprints()).isNotEmpty();
        assertThat(stakeholder.getMilestones()).isNotEmpty();
    }
}
