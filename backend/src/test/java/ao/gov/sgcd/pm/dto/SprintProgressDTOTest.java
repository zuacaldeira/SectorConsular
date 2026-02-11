package ao.gov.sgcd.pm.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SprintProgressDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        SprintProgressDTO dto = SprintProgressDTO.builder()
                .sprintNumber(1)
                .name("Sprint 1 - Fundacao")
                .totalSessions(36)
                .completedSessions(10)
                .totalHours(120)
                .actualHours(BigDecimal.valueOf(33.5))
                .progressPercent(27.78)
                .plannedTasks(20)
                .inProgressTasks(3)
                .completedTasks(10)
                .blockedTasks(2)
                .skippedTasks(1)
                .build();

        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", dto.getName());
        assertEquals(36, dto.getTotalSessions());
        assertEquals(10, dto.getCompletedSessions());
        assertEquals(120, dto.getTotalHours());
        assertEquals(BigDecimal.valueOf(33.5), dto.getActualHours());
        assertEquals(27.78, dto.getProgressPercent());
        assertEquals(20, dto.getPlannedTasks());
        assertEquals(3, dto.getInProgressTasks());
        assertEquals(10, dto.getCompletedTasks());
        assertEquals(2, dto.getBlockedTasks());
        assertEquals(1, dto.getSkippedTasks());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        SprintProgressDTO dto = new SprintProgressDTO();

        assertNull(dto.getSprintNumber());
        assertNull(dto.getName());
        assertNull(dto.getTotalSessions());
        assertNull(dto.getCompletedSessions());
        assertNull(dto.getTotalHours());
        assertNull(dto.getActualHours());
        assertNull(dto.getProgressPercent());
        assertNull(dto.getPlannedTasks());
        assertNull(dto.getInProgressTasks());
        assertNull(dto.getCompletedTasks());
        assertNull(dto.getBlockedTasks());
        assertNull(dto.getSkippedTasks());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        SprintProgressDTO dto = new SprintProgressDTO(
                1, "Sprint 1", 36, 10, 120,
                BigDecimal.valueOf(33.5), 27.78,
                20, 3, 10, 2, 1
        );

        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1", dto.getName());
        assertEquals(36, dto.getTotalSessions());
        assertEquals(10, dto.getCompletedSessions());
        assertEquals(120, dto.getTotalHours());
        assertEquals(BigDecimal.valueOf(33.5), dto.getActualHours());
        assertEquals(27.78, dto.getProgressPercent());
        assertEquals(20, dto.getPlannedTasks());
        assertEquals(3, dto.getInProgressTasks());
        assertEquals(10, dto.getCompletedTasks());
        assertEquals(2, dto.getBlockedTasks());
        assertEquals(1, dto.getSkippedTasks());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        SprintProgressDTO dto = new SprintProgressDTO();

        dto.setSprintNumber(2);
        dto.setName("Sprint 2");
        dto.setTotalSessions(40);
        dto.setCompletedSessions(20);
        dto.setTotalHours(140);
        dto.setActualHours(BigDecimal.valueOf(70.0));
        dto.setProgressPercent(50.0);
        dto.setPlannedTasks(15);
        dto.setInProgressTasks(5);
        dto.setCompletedTasks(20);
        dto.setBlockedTasks(0);
        dto.setSkippedTasks(0);

        assertEquals(2, dto.getSprintNumber());
        assertEquals("Sprint 2", dto.getName());
        assertEquals(40, dto.getTotalSessions());
        assertEquals(20, dto.getCompletedSessions());
        assertEquals(140, dto.getTotalHours());
        assertEquals(BigDecimal.valueOf(70.0), dto.getActualHours());
        assertEquals(50.0, dto.getProgressPercent());
        assertEquals(15, dto.getPlannedTasks());
        assertEquals(5, dto.getInProgressTasks());
        assertEquals(20, dto.getCompletedTasks());
        assertEquals(0, dto.getBlockedTasks());
        assertEquals(0, dto.getSkippedTasks());
    }

    @Test
    void equals_reflexive() {
        SprintProgressDTO dto = SprintProgressDTO.builder().sprintNumber(1).name("Sprint 1").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        SprintProgressDTO dto1 = SprintProgressDTO.builder().sprintNumber(1).name("Sprint 1").totalSessions(36).build();
        SprintProgressDTO dto2 = SprintProgressDTO.builder().sprintNumber(1).name("Sprint 1").totalSessions(36).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        SprintProgressDTO dto = SprintProgressDTO.builder().sprintNumber(1).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        SprintProgressDTO dto = SprintProgressDTO.builder().sprintNumber(1).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        SprintProgressDTO dto1 = SprintProgressDTO.builder().sprintNumber(1).name("Sprint 1").build();
        SprintProgressDTO dto2 = SprintProgressDTO.builder().sprintNumber(2).name("Sprint 2").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        SprintProgressDTO dto1 = SprintProgressDTO.builder().sprintNumber(1).name("Sprint 1").build();
        SprintProgressDTO dto2 = SprintProgressDTO.builder().sprintNumber(1).name("Sprint 1").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        SprintProgressDTO dto1 = SprintProgressDTO.builder().sprintNumber(1).build();
        SprintProgressDTO dto2 = SprintProgressDTO.builder().sprintNumber(2).build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        SprintProgressDTO dto = SprintProgressDTO.builder()
                .sprintNumber(1)
                .name("Sprint 1")
                .progressPercent(50.0)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("SprintProgressDTO"));
        assertTrue(result.contains("Sprint 1"));
        assertTrue(result.contains("50.0"));
    }
}
