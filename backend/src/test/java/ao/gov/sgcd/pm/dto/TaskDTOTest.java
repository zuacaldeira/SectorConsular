package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.TaskStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        LocalDate sessionDate = LocalDate.of(2026, 1, 6);
        LocalDateTime startedAt = LocalDateTime.of(2026, 1, 6, 9, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 1, 6, 12, 30);
        List<String> deliverables = List.of("Entity classes", "Repository interfaces");
        List<String> validationCriteria = List.of("Compiles", "Tests pass");
        List<TaskNoteDTO> notes = new ArrayList<>();
        List<TaskExecutionDTO> executions = new ArrayList<>();

        TaskDTO dto = TaskDTO.builder()
                .id(1L)
                .sprintId(1L)
                .sprintNumber(1)
                .sprintName("Sprint 1")
                .taskCode("S1-D01")
                .sessionDate(sessionDate)
                .dayOfWeek("MONDAY")
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .title("Configuracao Inicial")
                .titleEn("Initial Setup")
                .description("Setup the project")
                .deliverables(deliverables)
                .validationCriteria(validationCriteria)
                .coverageTarget("80%")
                .status(TaskStatus.COMPLETED)
                .actualHours(BigDecimal.valueOf(3.5))
                .startedAt(startedAt)
                .completedAt(completedAt)
                .completionNotes("All done")
                .blockers(null)
                .sortOrder(1)
                .notes(notes)
                .executions(executions)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getSprintId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1", dto.getSprintName());
        assertEquals("S1-D01", dto.getTaskCode());
        assertEquals(sessionDate, dto.getSessionDate());
        assertEquals("MONDAY", dto.getDayOfWeek());
        assertEquals(1, dto.getWeekNumber());
        assertEquals(BigDecimal.valueOf(3.5), dto.getPlannedHours());
        assertEquals("Configuracao Inicial", dto.getTitle());
        assertEquals("Initial Setup", dto.getTitleEn());
        assertEquals("Setup the project", dto.getDescription());
        assertEquals(deliverables, dto.getDeliverables());
        assertEquals(validationCriteria, dto.getValidationCriteria());
        assertEquals("80%", dto.getCoverageTarget());
        assertEquals(TaskStatus.COMPLETED, dto.getStatus());
        assertEquals(BigDecimal.valueOf(3.5), dto.getActualHours());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(completedAt, dto.getCompletedAt());
        assertEquals("All done", dto.getCompletionNotes());
        assertNull(dto.getBlockers());
        assertEquals(1, dto.getSortOrder());
        assertSame(notes, dto.getNotes());
        assertSame(executions, dto.getExecutions());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        TaskDTO dto = new TaskDTO();

        assertNull(dto.getId());
        assertNull(dto.getSprintId());
        assertNull(dto.getSprintNumber());
        assertNull(dto.getSprintName());
        assertNull(dto.getTaskCode());
        assertNull(dto.getSessionDate());
        assertNull(dto.getDayOfWeek());
        assertNull(dto.getWeekNumber());
        assertNull(dto.getPlannedHours());
        assertNull(dto.getTitle());
        assertNull(dto.getTitleEn());
        assertNull(dto.getDescription());
        assertNull(dto.getDeliverables());
        assertNull(dto.getValidationCriteria());
        assertNull(dto.getCoverageTarget());
        assertNull(dto.getStatus());
        assertNull(dto.getActualHours());
        assertNull(dto.getStartedAt());
        assertNull(dto.getCompletedAt());
        assertNull(dto.getCompletionNotes());
        assertNull(dto.getBlockers());
        assertNull(dto.getSortOrder());
        assertNull(dto.getNotes());
        assertNull(dto.getExecutions());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDate sessionDate = LocalDate.of(2026, 1, 7);
        LocalDateTime startedAt = LocalDateTime.of(2026, 1, 7, 10, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 1, 7, 13, 0);
        List<String> deliverables = List.of("Deliverable 1");
        List<String> validationCriteria = List.of("Criterion 1");
        List<TaskNoteDTO> notes = new ArrayList<>();
        List<TaskExecutionDTO> executions = new ArrayList<>();

        TaskDTO dto = new TaskDTO(
                2L, 1L, 1, "Sprint 1", "S1-D02",
                sessionDate, "TUESDAY", 1, BigDecimal.valueOf(3.0),
                "Titulo", "Title", "Desc",
                deliverables, validationCriteria, "90%",
                TaskStatus.IN_PROGRESS, BigDecimal.valueOf(2.0),
                startedAt, completedAt, "Notes", "Blocker",
                2, notes, executions
        );

        assertEquals(2L, dto.getId());
        assertEquals(1L, dto.getSprintId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1", dto.getSprintName());
        assertEquals("S1-D02", dto.getTaskCode());
        assertEquals(sessionDate, dto.getSessionDate());
        assertEquals("TUESDAY", dto.getDayOfWeek());
        assertEquals(1, dto.getWeekNumber());
        assertEquals(BigDecimal.valueOf(3.0), dto.getPlannedHours());
        assertEquals("Titulo", dto.getTitle());
        assertEquals("Title", dto.getTitleEn());
        assertEquals("Desc", dto.getDescription());
        assertEquals(deliverables, dto.getDeliverables());
        assertEquals(validationCriteria, dto.getValidationCriteria());
        assertEquals("90%", dto.getCoverageTarget());
        assertEquals(TaskStatus.IN_PROGRESS, dto.getStatus());
        assertEquals(BigDecimal.valueOf(2.0), dto.getActualHours());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(completedAt, dto.getCompletedAt());
        assertEquals("Notes", dto.getCompletionNotes());
        assertEquals("Blocker", dto.getBlockers());
        assertEquals(2, dto.getSortOrder());
        assertSame(notes, dto.getNotes());
        assertSame(executions, dto.getExecutions());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        TaskDTO dto = new TaskDTO();
        LocalDate sessionDate = LocalDate.of(2026, 2, 10);
        LocalDateTime startedAt = LocalDateTime.of(2026, 2, 10, 8, 0);
        LocalDateTime completedAt = LocalDateTime.of(2026, 2, 10, 11, 30);
        List<String> deliverables = List.of("D1", "D2");
        List<String> validationCriteria = List.of("V1");
        List<TaskNoteDTO> notes = new ArrayList<>();
        List<TaskExecutionDTO> executions = new ArrayList<>();

        dto.setId(5L);
        dto.setSprintId(2L);
        dto.setSprintNumber(2);
        dto.setSprintName("Sprint 2");
        dto.setTaskCode("S2-D10");
        dto.setSessionDate(sessionDate);
        dto.setDayOfWeek("WEDNESDAY");
        dto.setWeekNumber(3);
        dto.setPlannedHours(BigDecimal.valueOf(4.0));
        dto.setTitle("Titulo Teste");
        dto.setTitleEn("Test Title");
        dto.setDescription("Test description");
        dto.setDeliverables(deliverables);
        dto.setValidationCriteria(validationCriteria);
        dto.setCoverageTarget("75%");
        dto.setStatus(TaskStatus.BLOCKED);
        dto.setActualHours(BigDecimal.valueOf(1.5));
        dto.setStartedAt(startedAt);
        dto.setCompletedAt(completedAt);
        dto.setCompletionNotes("Partial");
        dto.setBlockers("DB issue");
        dto.setSortOrder(10);
        dto.setNotes(notes);
        dto.setExecutions(executions);

        assertEquals(5L, dto.getId());
        assertEquals(2L, dto.getSprintId());
        assertEquals(2, dto.getSprintNumber());
        assertEquals("Sprint 2", dto.getSprintName());
        assertEquals("S2-D10", dto.getTaskCode());
        assertEquals(sessionDate, dto.getSessionDate());
        assertEquals("WEDNESDAY", dto.getDayOfWeek());
        assertEquals(3, dto.getWeekNumber());
        assertEquals(BigDecimal.valueOf(4.0), dto.getPlannedHours());
        assertEquals("Titulo Teste", dto.getTitle());
        assertEquals("Test Title", dto.getTitleEn());
        assertEquals("Test description", dto.getDescription());
        assertEquals(deliverables, dto.getDeliverables());
        assertEquals(validationCriteria, dto.getValidationCriteria());
        assertEquals("75%", dto.getCoverageTarget());
        assertEquals(TaskStatus.BLOCKED, dto.getStatus());
        assertEquals(BigDecimal.valueOf(1.5), dto.getActualHours());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(completedAt, dto.getCompletedAt());
        assertEquals("Partial", dto.getCompletionNotes());
        assertEquals("DB issue", dto.getBlockers());
        assertEquals(10, dto.getSortOrder());
        assertSame(notes, dto.getNotes());
        assertSame(executions, dto.getExecutions());
    }

    @Test
    void equals_reflexive() {
        TaskDTO dto = TaskDTO.builder().id(1L).taskCode("S1-D01").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        TaskDTO dto1 = TaskDTO.builder().id(1L).taskCode("S1-D01").title("Title").build();
        TaskDTO dto2 = TaskDTO.builder().id(1L).taskCode("S1-D01").title("Title").build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        TaskDTO dto = TaskDTO.builder().id(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        TaskDTO dto = TaskDTO.builder().id(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        TaskDTO dto1 = TaskDTO.builder().id(1L).taskCode("S1-D01").build();
        TaskDTO dto2 = TaskDTO.builder().id(2L).taskCode("S1-D02").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        TaskDTO dto1 = TaskDTO.builder().id(1L).taskCode("S1-D01").title("Title").build();
        TaskDTO dto2 = TaskDTO.builder().id(1L).taskCode("S1-D01").title("Title").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        TaskDTO dto1 = TaskDTO.builder().id(1L).taskCode("S1-D01").build();
        TaskDTO dto2 = TaskDTO.builder().id(2L).taskCode("S1-D02").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        TaskDTO dto = TaskDTO.builder()
                .id(1L)
                .taskCode("S1-D01")
                .title("Configuracao Inicial")
                .build();

        String result = dto.toString();
        assertTrue(result.contains("TaskDTO"));
        assertTrue(result.contains("S1-D01"));
        assertTrue(result.contains("Configuracao Inicial"));
    }
}
