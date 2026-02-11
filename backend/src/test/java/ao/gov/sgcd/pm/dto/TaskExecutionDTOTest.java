package ao.gov.sgcd.pm.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskExecutionDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        LocalDateTime startedAt = LocalDateTime.of(2026, 1, 6, 9, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 1, 6, 12, 30);

        TaskExecutionDTO dto = TaskExecutionDTO.builder()
                .id(1L)
                .taskId(10L)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .hoursSpent(BigDecimal.valueOf(3.5))
                .promptUsed("Generate REST controllers for Sprint entity")
                .responseSummary("Created SprintController with CRUD endpoints")
                .notes("Clean implementation, all tests passing")
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getTaskId());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(endedAt, dto.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.5), dto.getHoursSpent());
        assertEquals("Generate REST controllers for Sprint entity", dto.getPromptUsed());
        assertEquals("Created SprintController with CRUD endpoints", dto.getResponseSummary());
        assertEquals("Clean implementation, all tests passing", dto.getNotes());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        TaskExecutionDTO dto = new TaskExecutionDTO();

        assertNull(dto.getId());
        assertNull(dto.getTaskId());
        assertNull(dto.getStartedAt());
        assertNull(dto.getEndedAt());
        assertNull(dto.getHoursSpent());
        assertNull(dto.getPromptUsed());
        assertNull(dto.getResponseSummary());
        assertNull(dto.getNotes());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDateTime startedAt = LocalDateTime.of(2026, 2, 10, 14, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 2, 10, 17, 0);

        TaskExecutionDTO dto = new TaskExecutionDTO(
                2L, 20L, startedAt, endedAt, BigDecimal.valueOf(3.0),
                "Prompt text", "Response summary", "Execution notes"
        );

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getTaskId());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(endedAt, dto.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.0), dto.getHoursSpent());
        assertEquals("Prompt text", dto.getPromptUsed());
        assertEquals("Response summary", dto.getResponseSummary());
        assertEquals("Execution notes", dto.getNotes());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        TaskExecutionDTO dto = new TaskExecutionDTO();
        LocalDateTime startedAt = LocalDateTime.of(2026, 3, 5, 8, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 3, 5, 11, 0);

        dto.setId(3L);
        dto.setTaskId(30L);
        dto.setStartedAt(startedAt);
        dto.setEndedAt(endedAt);
        dto.setHoursSpent(BigDecimal.valueOf(3.0));
        dto.setPromptUsed("Create service layer");
        dto.setResponseSummary("Service created with methods");
        dto.setNotes("Needs review");

        assertEquals(3L, dto.getId());
        assertEquals(30L, dto.getTaskId());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(endedAt, dto.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.0), dto.getHoursSpent());
        assertEquals("Create service layer", dto.getPromptUsed());
        assertEquals("Service created with methods", dto.getResponseSummary());
        assertEquals("Needs review", dto.getNotes());
    }

    @Test
    void equals_reflexive() {
        TaskExecutionDTO dto = TaskExecutionDTO.builder().id(1L).taskId(10L).build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        LocalDateTime time = LocalDateTime.of(2026, 1, 1, 9, 0);
        TaskExecutionDTO dto1 = TaskExecutionDTO.builder().id(1L).taskId(10L).startedAt(time).build();
        TaskExecutionDTO dto2 = TaskExecutionDTO.builder().id(1L).taskId(10L).startedAt(time).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        TaskExecutionDTO dto = TaskExecutionDTO.builder().id(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        TaskExecutionDTO dto = TaskExecutionDTO.builder().id(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        TaskExecutionDTO dto1 = TaskExecutionDTO.builder().id(1L).taskId(10L).build();
        TaskExecutionDTO dto2 = TaskExecutionDTO.builder().id(2L).taskId(20L).build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        TaskExecutionDTO dto1 = TaskExecutionDTO.builder().id(1L).taskId(10L).notes("Note").build();
        TaskExecutionDTO dto2 = TaskExecutionDTO.builder().id(1L).taskId(10L).notes("Note").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        TaskExecutionDTO dto1 = TaskExecutionDTO.builder().id(1L).build();
        TaskExecutionDTO dto2 = TaskExecutionDTO.builder().id(2L).build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        TaskExecutionDTO dto = TaskExecutionDTO.builder()
                .id(1L)
                .taskId(10L)
                .promptUsed("Generate entities")
                .build();

        String result = dto.toString();
        assertTrue(result.contains("TaskExecutionDTO"));
        assertTrue(result.contains("Generate entities"));
        assertTrue(result.contains("10"));
    }
}
