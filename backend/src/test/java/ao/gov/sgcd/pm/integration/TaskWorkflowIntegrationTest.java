package ao.gov.sgcd.pm.integration;

import ao.gov.sgcd.pm.dto.TaskDTO;
import ao.gov.sgcd.pm.dto.TaskUpdateDTO;
import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskStatus;
import ao.gov.sgcd.pm.exception.ResourceNotFoundException;
import ao.gov.sgcd.pm.repository.SprintRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import ao.gov.sgcd.pm.seed.DataSeeder;
import ao.gov.sgcd.pm.service.TaskService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskWorkflowIntegrationTest {

    @MockitoBean
    private DataSeeder dataSeeder;

    @Autowired
    private TaskService taskService;

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
    void fullTaskLifecycle_shouldTransitionPlannedToCompleted() {
        // Find a task that is PLANNED
        var page = taskService.findFiltered(null, TaskStatus.PLANNED, null, null,
                org.springframework.data.domain.PageRequest.of(0, 1));

        assertThat(page.getContent()).isNotEmpty();
        TaskDTO task = page.getContent().get(0);
        Long taskId = task.getId();

        assertThat(task.getStatus()).isEqualTo(TaskStatus.PLANNED);

        // Start the task
        TaskDTO started = taskService.startTask(taskId);
        assertThat(started.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(started.getStartedAt()).isNotNull();

        // Complete the task
        TaskUpdateDTO completeDto = TaskUpdateDTO.builder()
                .actualHours(BigDecimal.valueOf(3.0))
                .completionNotes("Integration test complete")
                .build();
        TaskDTO completed = taskService.completeTask(taskId, completeDto);
        assertThat(completed.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(completed.getCompletedAt()).isNotNull();
        assertThat(completed.getActualHours()).isEqualByComparingTo(BigDecimal.valueOf(3.0));
        assertThat(completed.getCompletionNotes()).isEqualTo("Integration test complete");
    }

    @Test
    void findById_nonExistentTask_shouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> taskService.findById(99999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Tarefa não encontrada");
    }

    @Test
    void blockTask_shouldSetStatusToBlocked() {
        var page = taskService.findFiltered(null, TaskStatus.PLANNED, null, null,
                org.springframework.data.domain.PageRequest.of(0, 1));
        assertThat(page.getContent()).isNotEmpty();

        TaskDTO task = page.getContent().get(0);
        TaskDTO blocked = taskService.blockTask(task.getId(), "Dependência externa");
        assertThat(blocked.getStatus()).isEqualTo(TaskStatus.BLOCKED);
        assertThat(blocked.getBlockers()).isEqualTo("Dependência externa");
    }
}
