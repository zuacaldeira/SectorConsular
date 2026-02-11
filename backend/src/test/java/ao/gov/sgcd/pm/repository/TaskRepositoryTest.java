package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SprintRepository sprintRepository;

    private Sprint defaultSprint;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        sprintRepository.deleteAll();
        defaultSprint = createSprint(1, SprintStatus.ACTIVE);
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

    private Task createTask(Sprint sprint, String taskCode, LocalDate sessionDate,
                            TaskStatus status, int sortOrder) {
        Task task = Task.builder()
                .sprint(sprint)
                .taskCode(taskCode)
                .sessionDate(sessionDate)
                .dayOfWeek("SEG")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .title("Tarefa " + taskCode)
                .titleEn("Task " + taskCode)
                .description("Description for " + taskCode)
                .status(status)
                .sortOrder(sortOrder)
                .build();
        return taskRepository.save(task);
    }

    private Task createCompletedTask(Sprint sprint, String taskCode, LocalDate sessionDate,
                                     int sortOrder, BigDecimal actualHours, LocalDateTime completedAt) {
        Task task = Task.builder()
                .sprint(sprint)
                .taskCode(taskCode)
                .sessionDate(sessionDate)
                .dayOfWeek("SEG")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .title("Tarefa " + taskCode)
                .titleEn("Task " + taskCode)
                .status(TaskStatus.COMPLETED)
                .actualHours(actualHours)
                .completedAt(completedAt)
                .sortOrder(sortOrder)
                .build();
        return taskRepository.save(task);
    }

    // ---- findByTaskCode ----

    @Test
    void findByTaskCode_shouldReturnTaskWhenExists() {
        Task saved = createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2),
                TaskStatus.PLANNED, 1);

        Optional<Task> result = taskRepository.findByTaskCode("S1-001");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getTaskCode()).isEqualTo("S1-001");
    }

    @Test
    void findByTaskCode_shouldReturnEmptyWhenNotExists() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2),
                TaskStatus.PLANNED, 1);

        Optional<Task> result = taskRepository.findByTaskCode("S9-999");

        assertThat(result).isEmpty();
    }

    @Test
    void findByTaskCode_shouldReturnCorrectTaskAmongMultiple() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        Task target = createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3),
                TaskStatus.IN_PROGRESS, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.COMPLETED, 3);

        Optional<Task> result = taskRepository.findByTaskCode("S1-002");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(target.getId());
        assertThat(result.get().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    // ---- findBySprintIdOrderBySortOrderAsc ----

    @Test
    void findBySprintIdOrderBySortOrderAsc_shouldReturnTasksSortedBySortOrder() {
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 2);

        List<Task> result = taskRepository.findBySprintIdOrderBySortOrderAsc(defaultSprint.getId());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getSortOrder()).isEqualTo(1);
        assertThat(result.get(1).getSortOrder()).isEqualTo(2);
        assertThat(result.get(2).getSortOrder()).isEqualTo(3);
    }

    @Test
    void findBySprintIdOrderBySortOrderAsc_shouldReturnEmptyForNonexistentSprint() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findBySprintIdOrderBySortOrderAsc(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findBySprintIdOrderBySortOrderAsc_shouldOnlyReturnTasksForGivenSprint() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 2);
        createTask(sprint2, "S2-001", LocalDate.of(2026, 5, 11), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findBySprintIdOrderBySortOrderAsc(defaultSprint.getId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(t -> t.getSprint().getId().equals(defaultSprint.getId()));
    }

    // ---- findBySprintIdAndStatus ----

    @Test
    void findBySprintIdAndStatus_shouldReturnMatchingTasks() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.COMPLETED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);

        List<Task> result = taskRepository.findBySprintIdAndStatus(
                defaultSprint.getId(), TaskStatus.PLANNED);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(t -> t.getStatus() == TaskStatus.PLANNED);
    }

    @Test
    void findBySprintIdAndStatus_shouldReturnOrderedBySortOrder() {
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findBySprintIdAndStatus(
                defaultSprint.getId(), TaskStatus.PLANNED);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSortOrder()).isEqualTo(1);
        assertThat(result.get(1).getSortOrder()).isEqualTo(3);
    }

    @Test
    void findBySprintIdAndStatus_shouldReturnEmptyWhenNoMatch() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findBySprintIdAndStatus(
                defaultSprint.getId(), TaskStatus.BLOCKED);

        assertThat(result).isEmpty();
    }

    // ---- findBySessionDate ----

    @Test
    void findBySessionDate_shouldReturnTaskForDate() {
        LocalDate date = LocalDate.of(2026, 3, 2);
        Task saved = createTask(defaultSprint, "S1-001", date, TaskStatus.PLANNED, 1);

        Optional<Task> result = taskRepository.findBySessionDate(date);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    void findBySessionDate_shouldReturnEmptyWhenNoTaskForDate() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        Optional<Task> result = taskRepository.findBySessionDate(LocalDate.of(2026, 6, 15));

        assertThat(result).isEmpty();
    }

    // ---- findUpcomingPlanned ----

    @Test
    void findUpcomingPlanned_shouldReturnPlannedTasksFromDateOnward() {
        LocalDate today = LocalDate.of(2026, 3, 5);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 5), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 6), TaskStatus.PLANNED, 3);
        createTask(defaultSprint, "S1-004", LocalDate.of(2026, 3, 7), TaskStatus.COMPLETED, 4);

        List<Task> result = taskRepository.findUpcomingPlanned(today);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-002");
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-003");
    }

    @Test
    void findUpcomingPlanned_shouldExcludeNonPlannedTasks() {
        LocalDate today = LocalDate.of(2026, 3, 1);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.IN_PROGRESS, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.COMPLETED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.BLOCKED, 3);
        createTask(defaultSprint, "S1-004", LocalDate.of(2026, 3, 5), TaskStatus.PLANNED, 4);

        List<Task> result = taskRepository.findUpcomingPlanned(today);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-004");
    }

    @Test
    void findUpcomingPlanned_shouldOrderBySessionDateThenSortOrder() {
        LocalDate today = LocalDate.of(2026, 3, 1);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findUpcomingPlanned(today);

        assertThat(result).hasSize(3);
        // First by date (2026-03-02), then by sortOrder
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-002"); // date 3/2, sort 1
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-001"); // date 3/2, sort 2
        assertThat(result.get(2).getTaskCode()).isEqualTo("S1-003"); // date 3/4, sort 1
    }

    @Test
    void findUpcomingPlanned_shouldReturnEmptyWhenNonePlanned() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.COMPLETED, 1);

        List<Task> result = taskRepository.findUpcomingPlanned(LocalDate.of(2026, 3, 1));

        assertThat(result).isEmpty();
    }

    // ---- findNextPlanned ----

    @Test
    void findNextPlanned_shouldReturnAllPlannedTasksOrderedByDateAndSortOrder() {
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 6), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-004", LocalDate.of(2026, 3, 4), TaskStatus.COMPLETED, 1);

        List<Task> result = taskRepository.findNextPlanned();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-001"); // earliest date
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-002"); // next date
        assertThat(result.get(2).getTaskCode()).isEqualTo("S1-003"); // latest date
    }

    @Test
    void findNextPlanned_shouldReturnEmptyWhenNoPlannedTasks() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.COMPLETED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.IN_PROGRESS, 2);

        List<Task> result = taskRepository.findNextPlanned();

        assertThat(result).isEmpty();
    }

    // ---- findRecentCompleted ----

    @Test
    void findRecentCompleted_shouldReturnTasksOrderedByCompletedAtDesc() {
        LocalDateTime early = LocalDateTime.of(2026, 3, 2, 10, 0);
        LocalDateTime mid = LocalDateTime.of(2026, 3, 3, 10, 0);
        LocalDateTime late = LocalDateTime.of(2026, 3, 4, 10, 0);

        createCompletedTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), 1,
                BigDecimal.valueOf(3.0), early);
        createCompletedTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), 3,
                BigDecimal.valueOf(4.0), late);
        createCompletedTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), 2,
                BigDecimal.valueOf(3.5), mid);

        List<Task> result = taskRepository.findRecentCompleted(PageRequest.of(0, 10));

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-003"); // latest completedAt
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-002");
        assertThat(result.get(2).getTaskCode()).isEqualTo("S1-001"); // earliest completedAt
    }

    @Test
    void findRecentCompleted_shouldRespectPageSize() {
        LocalDateTime time1 = LocalDateTime.of(2026, 3, 2, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2026, 3, 3, 10, 0);
        LocalDateTime time3 = LocalDateTime.of(2026, 3, 4, 10, 0);

        createCompletedTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), 1,
                BigDecimal.valueOf(3.0), time1);
        createCompletedTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), 2,
                BigDecimal.valueOf(3.5), time2);
        createCompletedTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), 3,
                BigDecimal.valueOf(4.0), time3);

        List<Task> result = taskRepository.findRecentCompleted(PageRequest.of(0, 2));

        assertThat(result).hasSize(2);
    }

    @Test
    void findRecentCompleted_shouldIncludeAllTasksNotJustCompleted() {
        // The query orders by completedAt DESC; it does not filter by status
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findRecentCompleted(PageRequest.of(0, 10));

        // The query returns all tasks, just ordered by completedAt DESC
        assertThat(result).hasSize(1);
    }

    // ---- findByDateRange ----

    @Test
    void findByDateRange_shouldReturnTasksInRange() {
        LocalDate from = LocalDate.of(2026, 3, 2);
        LocalDate to = LocalDate.of(2026, 3, 4);

        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 1), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 3);
        createTask(defaultSprint, "S1-004", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 4);
        createTask(defaultSprint, "S1-005", LocalDate.of(2026, 3, 5), TaskStatus.PLANNED, 5);

        List<Task> result = taskRepository.findByDateRange(from, to);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-002");
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-003");
        assertThat(result.get(2).getTaskCode()).isEqualTo("S1-004");
    }

    @Test
    void findByDateRange_shouldReturnEmptyWhenNoTasksInRange() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findByDateRange(
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        assertThat(result).isEmpty();
    }

    @Test
    void findByDateRange_shouldOrderBySessionDateThenSortOrder() {
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findByDateRange(
                LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 3));

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-003"); // date 3/2, sort 1
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-001"); // date 3/3, sort 1
        assertThat(result.get(2).getTaskCode()).isEqualTo("S1-002"); // date 3/3, sort 2
    }

    // ---- findFiltered ----

    @Test
    void findFiltered_shouldFilterBySprintId() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(sprint2, "S2-001", LocalDate.of(2026, 5, 11), TaskStatus.PLANNED, 1);

        Page<Task> result = taskRepository.findFiltered(
                defaultSprint.getId(), null, null, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTaskCode()).isEqualTo("S1-001");
    }

    @Test
    void findFiltered_shouldFilterByStatus() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.COMPLETED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);

        Page<Task> result = taskRepository.findFiltered(
                null, TaskStatus.PLANNED, null, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(t -> t.getStatus() == TaskStatus.PLANNED);
    }

    @Test
    void findFiltered_shouldFilterByDateRange() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 5), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 10), TaskStatus.PLANNED, 3);

        Page<Task> result = taskRepository.findFiltered(
                null, null, LocalDate.of(2026, 3, 3), LocalDate.of(2026, 3, 8),
                PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTaskCode()).isEqualTo("S1-002");
    }

    @Test
    void findFiltered_shouldReturnAllWhenAllFiltersNull() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.COMPLETED, 2);

        Page<Task> result = taskRepository.findFiltered(
                null, null, null, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void findFiltered_shouldCombineMultipleFilters() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 5), TaskStatus.COMPLETED, 2);
        createTask(sprint2, "S2-001", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 1);

        Page<Task> result = taskRepository.findFiltered(
                defaultSprint.getId(), TaskStatus.PLANNED, LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 10), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTaskCode()).isEqualTo("S1-001");
    }

    @Test
    void findFiltered_shouldSupportPagination() {
        for (int i = 1; i <= 5; i++) {
            createTask(defaultSprint, "S1-00" + i,
                    LocalDate.of(2026, 3, 1 + i), TaskStatus.PLANNED, i);
        }

        Page<Task> page0 = taskRepository.findFiltered(
                null, null, null, null, PageRequest.of(0, 2));
        Page<Task> page1 = taskRepository.findFiltered(
                null, null, null, null, PageRequest.of(1, 2));

        assertThat(page0.getContent()).hasSize(2);
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page0.getTotalElements()).isEqualTo(5);
        assertThat(page0.getTotalPages()).isEqualTo(3);
    }

    // ---- countBySprintIdAndStatus ----

    @Test
    void countBySprintIdAndStatus_shouldReturnCorrectCount() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.COMPLETED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);

        Integer count = taskRepository.countBySprintIdAndStatus(
                defaultSprint.getId(), TaskStatus.PLANNED);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void countBySprintIdAndStatus_shouldReturnZeroWhenNoMatch() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        Integer count = taskRepository.countBySprintIdAndStatus(
                defaultSprint.getId(), TaskStatus.BLOCKED);

        assertThat(count).isEqualTo(0);
    }

    @Test
    void countBySprintIdAndStatus_shouldNotCountTasksFromOtherSprints() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.COMPLETED, 1);
        createTask(sprint2, "S2-001", LocalDate.of(2026, 5, 11), TaskStatus.COMPLETED, 1);

        Integer count = taskRepository.countBySprintIdAndStatus(
                defaultSprint.getId(), TaskStatus.COMPLETED);

        assertThat(count).isEqualTo(1);
    }

    // ---- countByStatus ----

    @Test
    void countByStatus_shouldReturnTotalCountAcrossAllSprints() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.COMPLETED, 2);
        createTask(sprint2, "S2-001", LocalDate.of(2026, 5, 11), TaskStatus.PLANNED, 1);

        Integer count = taskRepository.countByStatus(TaskStatus.PLANNED);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void countByStatus_shouldReturnZeroWhenNoTasksWithStatus() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        Integer count = taskRepository.countByStatus(TaskStatus.SKIPPED);

        assertThat(count).isEqualTo(0);
    }

    // ---- sumActualHoursCompleted ----

    @Test
    void sumActualHoursCompleted_shouldSumHoursOfCompletedTasks() {
        createCompletedTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), 1,
                BigDecimal.valueOf(3.0), LocalDateTime.now());
        createCompletedTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), 2,
                BigDecimal.valueOf(4.5), LocalDateTime.now());
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);

        BigDecimal result = taskRepository.sumActualHoursCompleted();

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(7.5));
    }

    @Test
    void sumActualHoursCompleted_shouldReturnZeroWhenNoCompletedTasks() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        BigDecimal result = taskRepository.sumActualHoursCompleted();

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void sumActualHoursCompleted_shouldReturnZeroWhenNoTasks() {
        BigDecimal result = taskRepository.sumActualHoursCompleted();

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    // ---- findByWeek ----

    @Test
    void findByWeek_shouldReturnTasksWithinWeekRange() {
        LocalDate weekStart = LocalDate.of(2026, 3, 2);  // Monday
        LocalDate weekEnd = LocalDate.of(2026, 3, 6);    // Friday

        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 1), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 2);
        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 4), TaskStatus.PLANNED, 3);
        createTask(defaultSprint, "S1-004", LocalDate.of(2026, 3, 6), TaskStatus.PLANNED, 4);
        createTask(defaultSprint, "S1-005", LocalDate.of(2026, 3, 7), TaskStatus.PLANNED, 5);

        List<Task> result = taskRepository.findByWeek(weekStart, weekEnd);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTaskCode()).isEqualTo("S1-002");
        assertThat(result.get(1).getTaskCode()).isEqualTo("S1-003");
        assertThat(result.get(2).getTaskCode()).isEqualTo("S1-004");
    }

    @Test
    void findByWeek_shouldReturnEmptyWhenNoTasksInWeek() {
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);

        List<Task> result = taskRepository.findByWeek(
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 5));

        assertThat(result).isEmpty();
    }

    @Test
    void findByWeek_shouldOrderBySessionDateAsc() {
        LocalDate weekStart = LocalDate.of(2026, 3, 2);
        LocalDate weekEnd = LocalDate.of(2026, 3, 6);

        createTask(defaultSprint, "S1-003", LocalDate.of(2026, 3, 5), TaskStatus.PLANNED, 3);
        createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2), TaskStatus.PLANNED, 1);
        createTask(defaultSprint, "S1-002", LocalDate.of(2026, 3, 3), TaskStatus.PLANNED, 2);

        List<Task> result = taskRepository.findByWeek(weekStart, weekEnd);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getSessionDate()).isEqualTo(LocalDate.of(2026, 3, 2));
        assertThat(result.get(1).getSessionDate()).isEqualTo(LocalDate.of(2026, 3, 3));
        assertThat(result.get(2).getSessionDate()).isEqualTo(LocalDate.of(2026, 3, 5));
    }

    // ---- Basic JPA operations ----

    @Test
    void save_shouldPersistTaskAndSetId() {
        Task task = Task.builder()
                .sprint(defaultSprint)
                .taskCode("S1-001")
                .sessionDate(LocalDate.of(2026, 3, 2))
                .dayOfWeek("SEG")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .title("Configuracao do Projecto")
                .status(TaskStatus.PLANNED)
                .sortOrder(1)
                .build();

        Task saved = taskRepository.save(task);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnSavedTask() {
        Task saved = createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2),
                TaskStatus.PLANNED, 1);

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTaskCode()).isEqualTo("S1-001");
    }

    @Test
    void delete_shouldRemoveTask() {
        Task saved = createTask(defaultSprint, "S1-001", LocalDate.of(2026, 3, 2),
                TaskStatus.PLANNED, 1);

        taskRepository.delete(saved);

        Optional<Task> found = taskRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
