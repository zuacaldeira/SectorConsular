package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SprintTest {

    @Test
    void builder_shouldCreateSprintWithAllFields() {
        LocalDate start = LocalDate.of(2026, 1, 5);
        LocalDate end = LocalDate.of(2026, 2, 1);
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = new ArrayList<>();

        Sprint sprint = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1 - Fundacao")
                .nameEn("Sprint 1 - Foundation")
                .description("Foundation sprint")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(start)
                .endDate(end)
                .focus("Backend")
                .color("#CC092F")
                .status(SprintStatus.PLANNED)
                .actualHours(BigDecimal.valueOf(10.5))
                .completedSessions(5)
                .completionNotes("On track")
                .createdAt(now)
                .updatedAt(now)
                .tasks(tasks)
                .build();

        assertEquals(1L, sprint.getId());
        assertEquals(1, sprint.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", sprint.getName());
        assertEquals("Sprint 1 - Foundation", sprint.getNameEn());
        assertEquals("Foundation sprint", sprint.getDescription());
        assertEquals(4, sprint.getWeeks());
        assertEquals(120, sprint.getTotalHours());
        assertEquals(36, sprint.getTotalSessions());
        assertEquals(start, sprint.getStartDate());
        assertEquals(end, sprint.getEndDate());
        assertEquals("Backend", sprint.getFocus());
        assertEquals("#CC092F", sprint.getColor());
        assertEquals(SprintStatus.PLANNED, sprint.getStatus());
        assertEquals(BigDecimal.valueOf(10.5), sprint.getActualHours());
        assertEquals(5, sprint.getCompletedSessions());
        assertEquals("On track", sprint.getCompletionNotes());
        assertEquals(now, sprint.getCreatedAt());
        assertEquals(now, sprint.getUpdatedAt());
        assertSame(tasks, sprint.getTasks());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        Sprint sprint = new Sprint();
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 4, 1);
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = new ArrayList<>();

        sprint.setId(2L);
        sprint.setSprintNumber(2);
        sprint.setName("Sprint 2");
        sprint.setNameEn("Sprint 2 EN");
        sprint.setDescription("Second sprint");
        sprint.setWeeks(3);
        sprint.setTotalHours(100);
        sprint.setTotalSessions(30);
        sprint.setStartDate(start);
        sprint.setEndDate(end);
        sprint.setFocus("Frontend");
        sprint.setColor("#1A1A1A");
        sprint.setStatus(SprintStatus.ACTIVE);
        sprint.setActualHours(BigDecimal.valueOf(20.0));
        sprint.setCompletedSessions(10);
        sprint.setCompletionNotes("Halfway done");
        sprint.setCreatedAt(now);
        sprint.setUpdatedAt(now);
        sprint.setTasks(tasks);

        assertEquals(2L, sprint.getId());
        assertEquals(2, sprint.getSprintNumber());
        assertEquals("Sprint 2", sprint.getName());
        assertEquals("Sprint 2 EN", sprint.getNameEn());
        assertEquals("Second sprint", sprint.getDescription());
        assertEquals(3, sprint.getWeeks());
        assertEquals(100, sprint.getTotalHours());
        assertEquals(30, sprint.getTotalSessions());
        assertEquals(start, sprint.getStartDate());
        assertEquals(end, sprint.getEndDate());
        assertEquals("Frontend", sprint.getFocus());
        assertEquals("#1A1A1A", sprint.getColor());
        assertEquals(SprintStatus.ACTIVE, sprint.getStatus());
        assertEquals(BigDecimal.valueOf(20.0), sprint.getActualHours());
        assertEquals(10, sprint.getCompletedSessions());
        assertEquals("Halfway done", sprint.getCompletionNotes());
        assertEquals(now, sprint.getCreatedAt());
        assertEquals(now, sprint.getUpdatedAt());
        assertSame(tasks, sprint.getTasks());
    }

    @Test
    void noArgConstructor_shouldCreateEmptySprint() {
        Sprint sprint = new Sprint();

        assertNull(sprint.getId());
        assertNull(sprint.getSprintNumber());
        assertNull(sprint.getName());
        assertNull(sprint.getNameEn());
        assertNull(sprint.getDescription());
        assertNull(sprint.getWeeks());
        assertNull(sprint.getTotalHours());
        assertNull(sprint.getTotalSessions());
        assertNull(sprint.getStartDate());
        assertNull(sprint.getEndDate());
        assertNull(sprint.getFocus());
        assertNull(sprint.getColor());
        assertNull(sprint.getStatus());
        assertNull(sprint.getActualHours());
        assertNull(sprint.getCompletedSessions());
        assertNull(sprint.getCompletionNotes());
        assertNull(sprint.getCreatedAt());
        assertNull(sprint.getUpdatedAt());
        assertNotNull(sprint.getTasks());
        assertTrue(sprint.getTasks().isEmpty());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 6, 1);
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = new ArrayList<>();

        Sprint sprint = new Sprint(
                3L, 3, "Sprint 3", "Sprint 3 EN", "Third sprint",
                5, 150, 45, start, end, "Integration", "#F4B400",
                SprintStatus.COMPLETED, BigDecimal.valueOf(148.5), 44,
                "Almost complete", now, now, tasks
        );

        assertEquals(3L, sprint.getId());
        assertEquals(3, sprint.getSprintNumber());
        assertEquals("Sprint 3", sprint.getName());
        assertEquals("Sprint 3 EN", sprint.getNameEn());
        assertEquals("Third sprint", sprint.getDescription());
        assertEquals(5, sprint.getWeeks());
        assertEquals(150, sprint.getTotalHours());
        assertEquals(45, sprint.getTotalSessions());
        assertEquals(start, sprint.getStartDate());
        assertEquals(end, sprint.getEndDate());
        assertEquals("Integration", sprint.getFocus());
        assertEquals("#F4B400", sprint.getColor());
        assertEquals(SprintStatus.COMPLETED, sprint.getStatus());
        assertEquals(BigDecimal.valueOf(148.5), sprint.getActualHours());
        assertEquals(44, sprint.getCompletedSessions());
        assertEquals("Almost complete", sprint.getCompletionNotes());
        assertEquals(now, sprint.getCreatedAt());
        assertEquals(now, sprint.getUpdatedAt());
        assertSame(tasks, sprint.getTasks());
    }

    @Test
    void prePersist_shouldSetDefaults() {
        Sprint sprint = new Sprint();

        sprint.onCreate();

        assertNotNull(sprint.getCreatedAt());
        assertNotNull(sprint.getUpdatedAt());
        assertEquals(BigDecimal.ZERO, sprint.getActualHours());
        assertEquals(0, sprint.getCompletedSessions());
        assertEquals(SprintStatus.PLANNED, sprint.getStatus());
    }

    @Test
    void prePersist_shouldNotOverrideExistingValues() {
        Sprint sprint = new Sprint();
        sprint.setActualHours(BigDecimal.valueOf(50.0));
        sprint.setCompletedSessions(15);
        sprint.setStatus(SprintStatus.ACTIVE);

        sprint.onCreate();

        assertEquals(BigDecimal.valueOf(50.0), sprint.getActualHours());
        assertEquals(15, sprint.getCompletedSessions());
        assertEquals(SprintStatus.ACTIVE, sprint.getStatus());
    }

    @Test
    void preUpdate_shouldSetUpdatedAt() {
        Sprint sprint = new Sprint();
        assertNull(sprint.getUpdatedAt());

        sprint.onUpdate();

        assertNotNull(sprint.getUpdatedAt());
    }

    @Test
    void preUpdate_shouldRefreshUpdatedAt() {
        Sprint sprint = new Sprint();
        LocalDateTime oldTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        sprint.setUpdatedAt(oldTime);

        sprint.onUpdate();

        assertNotNull(sprint.getUpdatedAt());
        assertTrue(sprint.getUpdatedAt().isAfter(oldTime));
    }

    @Test
    void builderDefault_tasksShouldBeEmptyArrayList() {
        Sprint sprint = Sprint.builder().build();

        assertNotNull(sprint.getTasks());
        assertTrue(sprint.getTasks().isEmpty());
        assertInstanceOf(ArrayList.class, sprint.getTasks());
    }

    @Test
    void prePersist_createdAtAndUpdatedAtShouldBeCloseInTime() {
        Sprint sprint = new Sprint();

        sprint.onCreate();

        LocalDateTime createdAt = sprint.getCreatedAt();
        LocalDateTime updatedAt = sprint.getUpdatedAt();
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        assertTrue(Math.abs(java.time.Duration.between(createdAt, updatedAt).toMillis()) < 100);
    }
}
