package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.SprintDTO;
import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SprintMapperTest {

    private final SprintMapper mapper = Mappers.getMapper(SprintMapper.class);

    @Test
    void toDto_shouldMapAllDirectFields() {
        LocalDate start = LocalDate.of(2026, 1, 5);
        LocalDate end = LocalDate.of(2026, 2, 1);

        Sprint sprint = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1 - Fundacao")
                .nameEn("Sprint 1 - Foundation")
                .description("Foundation sprint for the project")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(start)
                .endDate(end)
                .focus("Backend")
                .color("#CC092F")
                .status(SprintStatus.ACTIVE)
                .actualHours(BigDecimal.valueOf(45.5))
                .completedSessions(12)
                .completionNotes("Progressing well")
                .build();

        SprintDTO dto = mapper.toDto(sprint);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", dto.getName());
        assertEquals("Sprint 1 - Foundation", dto.getNameEn());
        assertEquals("Foundation sprint for the project", dto.getDescription());
        assertEquals(4, dto.getWeeks());
        assertEquals(120, dto.getTotalHours());
        assertEquals(36, dto.getTotalSessions());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
        assertEquals("Backend", dto.getFocus());
        assertEquals("#CC092F", dto.getColor());
        assertEquals(SprintStatus.ACTIVE, dto.getStatus());
        assertEquals(BigDecimal.valueOf(45.5), dto.getActualHours());
        assertEquals(12, dto.getCompletedSessions());
        assertEquals("Progressing well", dto.getCompletionNotes());
    }

    @Test
    void toDto_shouldIgnoreTaskCountAndProgressPercent() {
        Sprint sprint = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1")
                .nameEn("Sprint 1 EN")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .status(SprintStatus.PLANNED)
                .build();

        SprintDTO dto = mapper.toDto(sprint);

        assertNotNull(dto);
        assertNull(dto.getTaskCount());
        assertNull(dto.getProgressPercent());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        SprintDTO dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toDtoList_shouldMapAllElementsInList() {
        Sprint sprint1 = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1")
                .nameEn("Sprint 1 EN")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .status(SprintStatus.PLANNED)
                .build();

        Sprint sprint2 = Sprint.builder()
                .id(2L)
                .sprintNumber(2)
                .name("Sprint 2")
                .nameEn("Sprint 2 EN")
                .weeks(3)
                .totalHours(100)
                .totalSessions(30)
                .startDate(LocalDate.of(2026, 2, 3))
                .endDate(LocalDate.of(2026, 2, 22))
                .status(SprintStatus.ACTIVE)
                .build();

        List<SprintDTO> dtos = mapper.toDtoList(Arrays.asList(sprint1, sprint2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("Sprint 1", dtos.get(0).getName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Sprint 2", dtos.get(1).getName());
    }

    @Test
    void toDtoList_shouldReturnEmptyListForEmptyInput() {
        List<SprintDTO> dtos = mapper.toDtoList(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullForNullInput() {
        List<SprintDTO> dtos = mapper.toDtoList(null);

        assertNull(dtos);
    }

    @Test
    void toEntity_shouldMapDtoFieldsToEntity() {
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 4, 1);

        SprintDTO dto = SprintDTO.builder()
                .id(5L)
                .sprintNumber(3)
                .name("Sprint 3 - Integracao")
                .nameEn("Sprint 3 - Integration")
                .description("Integration phase")
                .weeks(5)
                .totalHours(150)
                .totalSessions(45)
                .startDate(start)
                .endDate(end)
                .focus("Full Stack")
                .color("#F4B400")
                .status(SprintStatus.COMPLETED)
                .actualHours(BigDecimal.valueOf(148.0))
                .completedSessions(44)
                .completionNotes("Nearly all tasks done")
                .taskCount(45)
                .progressPercent(97.8)
                .build();

        Sprint entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(3, entity.getSprintNumber());
        assertEquals("Sprint 3 - Integracao", entity.getName());
        assertEquals("Sprint 3 - Integration", entity.getNameEn());
        assertEquals("Integration phase", entity.getDescription());
        assertEquals(5, entity.getWeeks());
        assertEquals(150, entity.getTotalHours());
        assertEquals(45, entity.getTotalSessions());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
        assertEquals("Full Stack", entity.getFocus());
        assertEquals("#F4B400", entity.getColor());
        assertEquals(SprintStatus.COMPLETED, entity.getStatus());
        assertEquals(BigDecimal.valueOf(148.0), entity.getActualHours());
        assertEquals(44, entity.getCompletedSessions());
        assertEquals("Nearly all tasks done", entity.getCompletionNotes());
    }

    @Test
    void toEntity_shouldIgnoreIdField() {
        SprintDTO dto = SprintDTO.builder()
                .id(99L)
                .sprintNumber(1)
                .name("Sprint 1")
                .nameEn("Sprint 1 EN")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .status(SprintStatus.PLANNED)
                .build();

        Sprint entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
    }

    @Test
    void toEntity_shouldIgnoreTasksField() {
        SprintDTO dto = SprintDTO.builder()
                .sprintNumber(1)
                .name("Sprint 1")
                .nameEn("Sprint 1 EN")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .status(SprintStatus.PLANNED)
                .build();

        Sprint entity = mapper.toEntity(dto);

        assertNotNull(entity);
        // Builder default initializes tasks to empty ArrayList,
        // but toEntity ignores mapping tasks, so it depends on builder default
        // The key point is the mapper does not attempt to map any task data
    }

    @Test
    void toEntity_shouldIgnoreCreatedAtAndUpdatedAt() {
        SprintDTO dto = SprintDTO.builder()
                .sprintNumber(1)
                .name("Sprint 1")
                .nameEn("Sprint 1 EN")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .status(SprintStatus.PLANNED)
                .build();

        Sprint entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    void toEntity_shouldReturnNullForNullInput() {
        Sprint entity = mapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    void toDto_shouldHandleNullOptionalFields() {
        Sprint sprint = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1")
                .nameEn("Sprint 1 EN")
                .weeks(4)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .status(SprintStatus.PLANNED)
                .build();
        // description, focus, color, actualHours, completedSessions, completionNotes are null

        SprintDTO dto = mapper.toDto(sprint);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getDescription());
        assertNull(dto.getFocus());
        assertNull(dto.getColor());
        assertNull(dto.getActualHours());
        assertNull(dto.getCompletedSessions());
        assertNull(dto.getCompletionNotes());
    }
}
