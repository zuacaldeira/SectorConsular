package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.SprintStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SprintDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        LocalDate start = LocalDate.of(2026, 1, 5);
        LocalDate end = LocalDate.of(2026, 2, 1);

        SprintDTO dto = SprintDTO.builder()
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
                .taskCount(36)
                .progressPercent(13.89)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", dto.getName());
        assertEquals("Sprint 1 - Foundation", dto.getNameEn());
        assertEquals("Foundation sprint", dto.getDescription());
        assertEquals(4, dto.getWeeks());
        assertEquals(120, dto.getTotalHours());
        assertEquals(36, dto.getTotalSessions());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
        assertEquals("Backend", dto.getFocus());
        assertEquals("#CC092F", dto.getColor());
        assertEquals(SprintStatus.PLANNED, dto.getStatus());
        assertEquals(BigDecimal.valueOf(10.5), dto.getActualHours());
        assertEquals(5, dto.getCompletedSessions());
        assertEquals("On track", dto.getCompletionNotes());
        assertEquals(36, dto.getTaskCount());
        assertEquals(13.89, dto.getProgressPercent());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        SprintDTO dto = new SprintDTO();

        assertNull(dto.getId());
        assertNull(dto.getSprintNumber());
        assertNull(dto.getName());
        assertNull(dto.getNameEn());
        assertNull(dto.getDescription());
        assertNull(dto.getWeeks());
        assertNull(dto.getTotalHours());
        assertNull(dto.getTotalSessions());
        assertNull(dto.getStartDate());
        assertNull(dto.getEndDate());
        assertNull(dto.getFocus());
        assertNull(dto.getColor());
        assertNull(dto.getStatus());
        assertNull(dto.getActualHours());
        assertNull(dto.getCompletedSessions());
        assertNull(dto.getCompletionNotes());
        assertNull(dto.getTaskCount());
        assertNull(dto.getProgressPercent());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDate start = LocalDate.of(2026, 1, 5);
        LocalDate end = LocalDate.of(2026, 2, 1);

        SprintDTO dto = new SprintDTO(
                1L, 1, "Sprint 1", "Sprint 1 EN", "Description",
                4, 120, 36, start, end, "Backend", "#CC092F",
                SprintStatus.ACTIVE, BigDecimal.valueOf(50.0), 15,
                "Notes", 36, 41.67
        );

        assertEquals(1L, dto.getId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1", dto.getName());
        assertEquals("Sprint 1 EN", dto.getNameEn());
        assertEquals("Description", dto.getDescription());
        assertEquals(4, dto.getWeeks());
        assertEquals(120, dto.getTotalHours());
        assertEquals(36, dto.getTotalSessions());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
        assertEquals("Backend", dto.getFocus());
        assertEquals("#CC092F", dto.getColor());
        assertEquals(SprintStatus.ACTIVE, dto.getStatus());
        assertEquals(BigDecimal.valueOf(50.0), dto.getActualHours());
        assertEquals(15, dto.getCompletedSessions());
        assertEquals("Notes", dto.getCompletionNotes());
        assertEquals(36, dto.getTaskCount());
        assertEquals(41.67, dto.getProgressPercent());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        SprintDTO dto = new SprintDTO();
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 4, 1);

        dto.setId(2L);
        dto.setSprintNumber(2);
        dto.setName("Sprint 2");
        dto.setNameEn("Sprint 2 EN");
        dto.setDescription("Second sprint");
        dto.setWeeks(3);
        dto.setTotalHours(100);
        dto.setTotalSessions(30);
        dto.setStartDate(start);
        dto.setEndDate(end);
        dto.setFocus("Frontend");
        dto.setColor("#1A1A1A");
        dto.setStatus(SprintStatus.COMPLETED);
        dto.setActualHours(BigDecimal.valueOf(95.5));
        dto.setCompletedSessions(28);
        dto.setCompletionNotes("Almost done");
        dto.setTaskCount(30);
        dto.setProgressPercent(93.33);

        assertEquals(2L, dto.getId());
        assertEquals(2, dto.getSprintNumber());
        assertEquals("Sprint 2", dto.getName());
        assertEquals("Sprint 2 EN", dto.getNameEn());
        assertEquals("Second sprint", dto.getDescription());
        assertEquals(3, dto.getWeeks());
        assertEquals(100, dto.getTotalHours());
        assertEquals(30, dto.getTotalSessions());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
        assertEquals("Frontend", dto.getFocus());
        assertEquals("#1A1A1A", dto.getColor());
        assertEquals(SprintStatus.COMPLETED, dto.getStatus());
        assertEquals(BigDecimal.valueOf(95.5), dto.getActualHours());
        assertEquals(28, dto.getCompletedSessions());
        assertEquals("Almost done", dto.getCompletionNotes());
        assertEquals(30, dto.getTaskCount());
        assertEquals(93.33, dto.getProgressPercent());
    }

    @Test
    void equals_reflexive() {
        SprintDTO dto = SprintDTO.builder().id(1L).name("Sprint 1").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        SprintDTO dto1 = SprintDTO.builder().id(1L).name("Sprint 1").sprintNumber(1).build();
        SprintDTO dto2 = SprintDTO.builder().id(1L).name("Sprint 1").sprintNumber(1).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        SprintDTO dto = SprintDTO.builder().id(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        SprintDTO dto = SprintDTO.builder().id(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        SprintDTO dto1 = SprintDTO.builder().id(1L).name("Sprint 1").build();
        SprintDTO dto2 = SprintDTO.builder().id(2L).name("Sprint 2").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        SprintDTO dto1 = SprintDTO.builder().id(1L).name("Sprint 1").sprintNumber(1).build();
        SprintDTO dto2 = SprintDTO.builder().id(1L).name("Sprint 1").sprintNumber(1).build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        SprintDTO dto1 = SprintDTO.builder().id(1L).name("Sprint 1").build();
        SprintDTO dto2 = SprintDTO.builder().id(2L).name("Sprint 2").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        SprintDTO dto = SprintDTO.builder()
                .id(1L)
                .name("Sprint 1")
                .sprintNumber(1)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("SprintDTO"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Sprint 1"));
    }
}
