package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void builder_shouldCreateTaskWithAllFields() {
        Sprint sprint = new Sprint();
        LocalDate sessionDate = LocalDate.of(2026, 1, 6);
        LocalDateTime now = LocalDateTime.now();
        List<TaskExecution> executions = new ArrayList<>();
        List<TaskNote> notes = new ArrayList<>();

        Task task = Task.builder()
                .id(1L)
                .sprint(sprint)
                .taskCode("S1-001")
                .sessionDate(sessionDate)
                .dayOfWeek("SEG")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .title("Configurar projecto")
                .titleEn("Configure project")
                .description("Initial setup")
                .deliverables("[\"pom.xml\"]")
                .validationCriteria("[\"builds\"]")
                .coverageTarget("80%")
                .status(TaskStatus.PLANNED)
                .actualHours(BigDecimal.valueOf(3.0))
                .startedAt(now)
                .completedAt(now)
                .completionNotes("Done")
                .blockers("None")
                .promptTemplate("Setup prompt")
                .sortOrder(1)
                .createdAt(now)
                .updatedAt(now)
                .executions(executions)
                .notes(notes)
                .build();

        assertEquals(1L, task.getId());
        assertSame(sprint, task.getSprint());
        assertEquals("S1-001", task.getTaskCode());
        assertEquals(sessionDate, task.getSessionDate());
        assertEquals("SEG", task.getDayOfWeek());
        assertEquals(1, task.getWeekNumber());
        assertEquals(BigDecimal.valueOf(3.5), task.getPlannedHours());
        assertEquals("Configurar projecto", task.getTitle());
        assertEquals("Configure project", task.getTitleEn());
        assertEquals("Initial setup", task.getDescription());
        assertEquals("[\"pom.xml\"]", task.getDeliverables());
        assertEquals("[\"builds\"]", task.getValidationCriteria());
        assertEquals("80%", task.getCoverageTarget());
        assertEquals(TaskStatus.PLANNED, task.getStatus());
        assertEquals(BigDecimal.valueOf(3.0), task.getActualHours());
        assertEquals(now, task.getStartedAt());
        assertEquals(now, task.getCompletedAt());
        assertEquals("Done", task.getCompletionNotes());
        assertEquals("None", task.getBlockers());
        assertEquals("Setup prompt", task.getPromptTemplate());
        assertEquals(1, task.getSortOrder());
        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
        assertSame(executions, task.getExecutions());
        assertSame(notes, task.getNotes());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        Task task = new Task();
        Sprint sprint = new Sprint();
        LocalDate sessionDate = LocalDate.of(2026, 2, 10);
        LocalDateTime now = LocalDateTime.now();
        List<TaskExecution> executions = new ArrayList<>();
        List<TaskNote> notes = new ArrayList<>();

        task.setId(2L);
        task.setSprint(sprint);
        task.setTaskCode("S2-015");
        task.setSessionDate(sessionDate);
        task.setDayOfWeek("TER");
        task.setWeekNumber(3);
        task.setPlannedHours(BigDecimal.valueOf(4.0));
        task.setTitle("Implementar API");
        task.setTitleEn("Implement API");
        task.setDescription("REST endpoints");
        task.setDeliverables("[\"Controller.java\"]");
        task.setValidationCriteria("[\"tests pass\"]");
        task.setCoverageTarget("90%");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setActualHours(BigDecimal.valueOf(2.5));
        task.setStartedAt(now);
        task.setCompletedAt(now);
        task.setCompletionNotes("Partial");
        task.setBlockers("DB connection");
        task.setPromptTemplate("API prompt");
        task.setSortOrder(15);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        task.setExecutions(executions);
        task.setNotes(notes);

        assertEquals(2L, task.getId());
        assertSame(sprint, task.getSprint());
        assertEquals("S2-015", task.getTaskCode());
        assertEquals(sessionDate, task.getSessionDate());
        assertEquals("TER", task.getDayOfWeek());
        assertEquals(3, task.getWeekNumber());
        assertEquals(BigDecimal.valueOf(4.0), task.getPlannedHours());
        assertEquals("Implementar API", task.getTitle());
        assertEquals("Implement API", task.getTitleEn());
        assertEquals("REST endpoints", task.getDescription());
        assertEquals("[\"Controller.java\"]", task.getDeliverables());
        assertEquals("[\"tests pass\"]", task.getValidationCriteria());
        assertEquals("90%", task.getCoverageTarget());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(BigDecimal.valueOf(2.5), task.getActualHours());
        assertEquals(now, task.getStartedAt());
        assertEquals(now, task.getCompletedAt());
        assertEquals("Partial", task.getCompletionNotes());
        assertEquals("DB connection", task.getBlockers());
        assertEquals("API prompt", task.getPromptTemplate());
        assertEquals(15, task.getSortOrder());
        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
        assertSame(executions, task.getExecutions());
        assertSame(notes, task.getNotes());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyTask() {
        Task task = new Task();

        assertNull(task.getId());
        assertNull(task.getSprint());
        assertNull(task.getTaskCode());
        assertNull(task.getSessionDate());
        assertNull(task.getDayOfWeek());
        assertNull(task.getWeekNumber());
        assertNull(task.getPlannedHours());
        assertNull(task.getTitle());
        assertNull(task.getTitleEn());
        assertNull(task.getDescription());
        assertNull(task.getDeliverables());
        assertNull(task.getValidationCriteria());
        assertNull(task.getCoverageTarget());
        assertNull(task.getStatus());
        assertNull(task.getActualHours());
        assertNull(task.getStartedAt());
        assertNull(task.getCompletedAt());
        assertNull(task.getCompletionNotes());
        assertNull(task.getBlockers());
        assertNull(task.getPromptTemplate());
        assertNull(task.getSortOrder());
        assertNull(task.getCreatedAt());
        assertNull(task.getUpdatedAt());
        assertNotNull(task.getExecutions());
        assertTrue(task.getExecutions().isEmpty());
        assertNotNull(task.getNotes());
        assertTrue(task.getNotes().isEmpty());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        Sprint sprint = new Sprint();
        LocalDate sessionDate = LocalDate.of(2026, 3, 15);
        LocalDateTime now = LocalDateTime.now();
        List<TaskExecution> executions = new ArrayList<>();
        List<TaskNote> notes = new ArrayList<>();

        Task task = new Task(
                3L, sprint, "S3-042", sessionDate, "QUA", 7,
                BigDecimal.valueOf(3.0), "Testar servico", "Test service",
                "Unit tests", "[\"ServiceTest.java\"]", "[\"coverage >= 80%\"]",
                "85%", TaskStatus.COMPLETED, BigDecimal.valueOf(3.5),
                now, now, "All good", "None", "Test prompt",
                42, now, now, executions, notes
        );

        assertEquals(3L, task.getId());
        assertSame(sprint, task.getSprint());
        assertEquals("S3-042", task.getTaskCode());
        assertEquals(sessionDate, task.getSessionDate());
        assertEquals("QUA", task.getDayOfWeek());
        assertEquals(7, task.getWeekNumber());
        assertEquals(BigDecimal.valueOf(3.0), task.getPlannedHours());
        assertEquals("Testar servico", task.getTitle());
        assertEquals("Test service", task.getTitleEn());
        assertEquals("Unit tests", task.getDescription());
        assertEquals("[\"ServiceTest.java\"]", task.getDeliverables());
        assertEquals("[\"coverage >= 80%\"]", task.getValidationCriteria());
        assertEquals("85%", task.getCoverageTarget());
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        assertEquals(BigDecimal.valueOf(3.5), task.getActualHours());
        assertEquals(now, task.getStartedAt());
        assertEquals(now, task.getCompletedAt());
        assertEquals("All good", task.getCompletionNotes());
        assertEquals("None", task.getBlockers());
        assertEquals("Test prompt", task.getPromptTemplate());
        assertEquals(42, task.getSortOrder());
        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
        assertSame(executions, task.getExecutions());
        assertSame(notes, task.getNotes());
    }

    @Test
    void prePersist_shouldSetDefaults() {
        Task task = new Task();

        task.onCreate();

        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertEquals(TaskStatus.PLANNED, task.getStatus());
        assertEquals(0, task.getSortOrder());
    }

    @Test
    void prePersist_shouldNotOverrideExistingValues() {
        Task task = new Task();
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setSortOrder(5);

        task.onCreate();

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(5, task.getSortOrder());
    }

    @Test
    void preUpdate_shouldSetUpdatedAt() {
        Task task = new Task();
        assertNull(task.getUpdatedAt());

        task.onUpdate();

        assertNotNull(task.getUpdatedAt());
    }

    @Test
    void preUpdate_shouldRefreshUpdatedAt() {
        Task task = new Task();
        LocalDateTime oldTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        task.setUpdatedAt(oldTime);

        task.onUpdate();

        assertNotNull(task.getUpdatedAt());
        assertTrue(task.getUpdatedAt().isAfter(oldTime));
    }

    @Test
    void builderDefault_executionsShouldBeEmptyArrayList() {
        Task task = Task.builder().build();

        assertNotNull(task.getExecutions());
        assertTrue(task.getExecutions().isEmpty());
        assertInstanceOf(ArrayList.class, task.getExecutions());
    }

    @Test
    void builderDefault_notesShouldBeEmptyArrayList() {
        Task task = Task.builder().build();

        assertNotNull(task.getNotes());
        assertTrue(task.getNotes().isEmpty());
        assertInstanceOf(ArrayList.class, task.getNotes());
    }

    @Test
    void prePersist_createdAtAndUpdatedAtShouldBeCloseInTime() {
        Task task = new Task();

        task.onCreate();

        LocalDateTime createdAt = task.getCreatedAt();
        LocalDateTime updatedAt = task.getUpdatedAt();
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        assertTrue(Math.abs(java.time.Duration.between(createdAt, updatedAt).toMillis()) < 100);
    }
}
