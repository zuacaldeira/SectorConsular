package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskExecutionTest {

    @Test
    void builder_shouldCreateTaskExecutionWithAllFields() {
        Task task = new Task();
        LocalDateTime startedAt = LocalDateTime.of(2026, 1, 6, 9, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 1, 6, 12, 30);
        LocalDateTime createdAt = LocalDateTime.now();

        TaskExecution execution = TaskExecution.builder()
                .id(1L)
                .task(task)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .hoursSpent(BigDecimal.valueOf(3.5))
                .promptUsed("Generate entity classes")
                .responseSummary("Created 8 entity classes")
                .notes("Smooth session")
                .createdAt(createdAt)
                .build();

        assertEquals(1L, execution.getId());
        assertSame(task, execution.getTask());
        assertEquals(startedAt, execution.getStartedAt());
        assertEquals(endedAt, execution.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.5), execution.getHoursSpent());
        assertEquals("Generate entity classes", execution.getPromptUsed());
        assertEquals("Created 8 entity classes", execution.getResponseSummary());
        assertEquals("Smooth session", execution.getNotes());
        assertEquals(createdAt, execution.getCreatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        TaskExecution execution = new TaskExecution();
        Task task = new Task();
        LocalDateTime startedAt = LocalDateTime.of(2026, 2, 10, 14, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 2, 10, 17, 30);
        LocalDateTime createdAt = LocalDateTime.now();

        execution.setId(2L);
        execution.setTask(task);
        execution.setStartedAt(startedAt);
        execution.setEndedAt(endedAt);
        execution.setHoursSpent(BigDecimal.valueOf(3.5));
        execution.setPromptUsed("Implement REST API");
        execution.setResponseSummary("Created controllers");
        execution.setNotes("Good progress");
        execution.setCreatedAt(createdAt);

        assertEquals(2L, execution.getId());
        assertSame(task, execution.getTask());
        assertEquals(startedAt, execution.getStartedAt());
        assertEquals(endedAt, execution.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.5), execution.getHoursSpent());
        assertEquals("Implement REST API", execution.getPromptUsed());
        assertEquals("Created controllers", execution.getResponseSummary());
        assertEquals("Good progress", execution.getNotes());
        assertEquals(createdAt, execution.getCreatedAt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyTaskExecution() {
        TaskExecution execution = new TaskExecution();

        assertNull(execution.getId());
        assertNull(execution.getTask());
        assertNull(execution.getStartedAt());
        assertNull(execution.getEndedAt());
        assertNull(execution.getHoursSpent());
        assertNull(execution.getPromptUsed());
        assertNull(execution.getResponseSummary());
        assertNull(execution.getNotes());
        assertNull(execution.getCreatedAt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        Task task = new Task();
        LocalDateTime startedAt = LocalDateTime.of(2026, 3, 15, 10, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 3, 15, 13, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        TaskExecution execution = new TaskExecution(
                3L, task, startedAt, endedAt,
                BigDecimal.valueOf(3.0), "Test prompt",
                "Test summary", "Test notes", createdAt
        );

        assertEquals(3L, execution.getId());
        assertSame(task, execution.getTask());
        assertEquals(startedAt, execution.getStartedAt());
        assertEquals(endedAt, execution.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.0), execution.getHoursSpent());
        assertEquals("Test prompt", execution.getPromptUsed());
        assertEquals("Test summary", execution.getResponseSummary());
        assertEquals("Test notes", execution.getNotes());
        assertEquals(createdAt, execution.getCreatedAt());
    }

    @Test
    void prePersist_shouldSetCreatedAt() {
        TaskExecution execution = new TaskExecution();
        assertNull(execution.getCreatedAt());

        execution.onCreate();

        assertNotNull(execution.getCreatedAt());
    }

    @Test
    void prePersist_createdAtShouldBeCurrentTime() {
        TaskExecution execution = new TaskExecution();
        LocalDateTime before = LocalDateTime.now();

        execution.onCreate();

        LocalDateTime after = LocalDateTime.now();
        assertNotNull(execution.getCreatedAt());
        assertFalse(execution.getCreatedAt().isBefore(before));
        assertFalse(execution.getCreatedAt().isAfter(after));
    }
}
