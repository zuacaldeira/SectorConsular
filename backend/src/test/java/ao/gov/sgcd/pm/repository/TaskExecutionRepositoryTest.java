package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskExecutionRepositoryTest {

    @Autowired
    private TaskExecutionRepository taskExecutionRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SprintRepository sprintRepository;

    private Sprint defaultSprint;
    private Task defaultTask;

    @BeforeEach
    void setUp() {
        taskExecutionRepository.deleteAll();
        taskRepository.deleteAll();
        sprintRepository.deleteAll();

        defaultSprint = createSprint(1, SprintStatus.ACTIVE);
        defaultTask = createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2));
    }

    // ---- Helper Methods ----

    private Sprint createSprint(int number, SprintStatus status) {
        Sprint sprint = Sprint.builder()
                .sprintNumber(number)
                .name("Sprint " + number)
                .nameEn("Sprint " + number + " EN")
                .weeks(6)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 5, 10))
                .focus("Test")
                .color("#3884F4")
                .status(status)
                .build();
        return sprintRepository.save(sprint);
    }

    private Task createTask(Sprint sprint, String taskCode, LocalDate sessionDate) {
        Task task = Task.builder()
                .sprint(sprint)
                .taskCode(taskCode)
                .sessionDate(sessionDate)
                .dayOfWeek("SEG")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .title("Tarefa " + taskCode)
                .status(TaskStatus.IN_PROGRESS)
                .sortOrder(1)
                .build();
        return taskRepository.save(task);
    }

    private TaskExecution createExecution(Task task, LocalDateTime startedAt,
                                          LocalDateTime endedAt, BigDecimal hoursSpent) {
        TaskExecution execution = TaskExecution.builder()
                .task(task)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .hoursSpent(hoursSpent)
                .promptUsed("Test prompt for execution")
                .responseSummary("Test response summary")
                .notes("Execution notes")
                .build();
        return taskExecutionRepository.save(execution);
    }

    // ---- findByTaskIdOrderByStartedAtDesc ----

    @Test
    void findByTaskIdOrderByStartedAtDesc_shouldReturnExecutionsForTask() {
        LocalDateTime start1 = LocalDateTime.of(2026, 3, 2, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 3, 2, 12, 0);
        LocalDateTime start2 = LocalDateTime.of(2026, 3, 2, 14, 0);
        LocalDateTime end2 = LocalDateTime.of(2026, 3, 2, 17, 0);

        createExecution(defaultTask, start1, end1, BigDecimal.valueOf(3.0));
        createExecution(defaultTask, start2, end2, BigDecimal.valueOf(3.0));

        List<TaskExecution> result = taskExecutionRepository
                .findByTaskIdOrderByStartedAtDesc(defaultTask.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void findByTaskIdOrderByStartedAtDesc_shouldOrderByStartedAtDescending() {
        LocalDateTime earlyStart = LocalDateTime.of(2026, 3, 2, 9, 0);
        LocalDateTime earlyEnd = LocalDateTime.of(2026, 3, 2, 12, 0);
        LocalDateTime lateStart = LocalDateTime.of(2026, 3, 3, 9, 0);
        LocalDateTime lateEnd = LocalDateTime.of(2026, 3, 3, 12, 0);
        LocalDateTime midStart = LocalDateTime.of(2026, 3, 2, 14, 0);
        LocalDateTime midEnd = LocalDateTime.of(2026, 3, 2, 17, 0);

        createExecution(defaultTask, earlyStart, earlyEnd, BigDecimal.valueOf(3.0));
        createExecution(defaultTask, lateStart, lateEnd, BigDecimal.valueOf(3.0));
        createExecution(defaultTask, midStart, midEnd, BigDecimal.valueOf(3.0));

        List<TaskExecution> result = taskExecutionRepository
                .findByTaskIdOrderByStartedAtDesc(defaultTask.getId());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getStartedAt()).isEqualTo(lateStart);
        assertThat(result.get(1).getStartedAt()).isEqualTo(midStart);
        assertThat(result.get(2).getStartedAt()).isEqualTo(earlyStart);
    }

    @Test
    void findByTaskIdOrderByStartedAtDesc_shouldReturnEmptyForTaskWithNoExecutions() {
        List<TaskExecution> result = taskExecutionRepository
                .findByTaskIdOrderByStartedAtDesc(defaultTask.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByTaskIdOrderByStartedAtDesc_shouldReturnEmptyForNonexistentTask() {
        List<TaskExecution> result = taskExecutionRepository
                .findByTaskIdOrderByStartedAtDesc(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByTaskIdOrderByStartedAtDesc_shouldNotReturnExecutionsFromOtherTasks() {
        Task otherTask = createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3));

        LocalDateTime start1 = LocalDateTime.of(2026, 3, 2, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2026, 3, 2, 12, 0);
        LocalDateTime start2 = LocalDateTime.of(2026, 3, 3, 9, 0);
        LocalDateTime end2 = LocalDateTime.of(2026, 3, 3, 12, 0);

        createExecution(defaultTask, start1, end1, BigDecimal.valueOf(3.0));
        createExecution(otherTask, start2, end2, BigDecimal.valueOf(3.5));

        List<TaskExecution> result = taskExecutionRepository
                .findByTaskIdOrderByStartedAtDesc(defaultTask.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTask().getId()).isEqualTo(defaultTask.getId());
    }

    // ---- Basic JPA operations ----

    @Test
    void save_shouldPersistExecutionAndSetId() {
        TaskExecution execution = TaskExecution.builder()
                .task(defaultTask)
                .startedAt(LocalDateTime.of(2026, 3, 2, 9, 0))
                .endedAt(LocalDateTime.of(2026, 3, 2, 12, 30))
                .hoursSpent(BigDecimal.valueOf(3.5))
                .promptUsed("Implement the login feature")
                .responseSummary("Successfully implemented JWT auth")
                .notes("Went smoothly")
                .build();

        TaskExecution saved = taskExecutionRepository.save(execution);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getPromptUsed()).isEqualTo("Implement the login feature");
    }

    @Test
    void findById_shouldReturnSavedExecution() {
        TaskExecution saved = createExecution(defaultTask,
                LocalDateTime.of(2026, 3, 2, 9, 0),
                LocalDateTime.of(2026, 3, 2, 12, 0),
                BigDecimal.valueOf(3.0));

        Optional<TaskExecution> found = taskExecutionRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getHoursSpent()).isEqualByComparingTo(BigDecimal.valueOf(3.0));
        assertThat(found.get().getResponseSummary()).isEqualTo("Test response summary");
    }

    @Test
    void delete_shouldRemoveExecution() {
        TaskExecution saved = createExecution(defaultTask,
                LocalDateTime.of(2026, 3, 2, 9, 0),
                LocalDateTime.of(2026, 3, 2, 12, 0),
                BigDecimal.valueOf(3.0));

        taskExecutionRepository.delete(saved);

        Optional<TaskExecution> found = taskExecutionRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void save_shouldAllowNullEndedAt() {
        TaskExecution execution = TaskExecution.builder()
                .task(defaultTask)
                .startedAt(LocalDateTime.of(2026, 3, 2, 9, 0))
                .build();

        TaskExecution saved = taskExecutionRepository.save(execution);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEndedAt()).isNull();
        assertThat(saved.getHoursSpent()).isNull();
    }

    @Test
    void count_shouldReturnCorrectCount() {
        createExecution(defaultTask,
                LocalDateTime.of(2026, 3, 2, 9, 0),
                LocalDateTime.of(2026, 3, 2, 12, 0),
                BigDecimal.valueOf(3.0));
        createExecution(defaultTask,
                LocalDateTime.of(2026, 3, 3, 9, 0),
                LocalDateTime.of(2026, 3, 3, 12, 0),
                BigDecimal.valueOf(3.0));

        long count = taskExecutionRepository.count();

        assertThat(count).isEqualTo(2);
    }
}
