package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.TaskExecutionDTO;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskExecution;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskExecutionMapperTest {

    private final TaskExecutionMapper mapper = Mappers.getMapper(TaskExecutionMapper.class);

    @Test
    void toDto_shouldMapAllFieldsIncludingTaskId() {
        Task task = Task.builder().id(15L).build();
        LocalDateTime startedAt = LocalDateTime.of(2026, 2, 10, 9, 0, 0);
        LocalDateTime endedAt = LocalDateTime.of(2026, 2, 10, 12, 30, 0);
        LocalDateTime createdAt = LocalDateTime.of(2026, 2, 10, 9, 0, 0);

        TaskExecution execution = TaskExecution.builder()
                .id(1L)
                .task(task)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .hoursSpent(BigDecimal.valueOf(3.5))
                .promptUsed("Implement the user authentication module with JWT")
                .responseSummary("Created JwtService, SecurityConfig, and AuthController")
                .notes("Used Spring Security 6.x patterns")
                .createdAt(createdAt)
                .build();

        TaskExecutionDTO dto = mapper.toDto(execution);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(15L, dto.getTaskId());
        assertEquals(startedAt, dto.getStartedAt());
        assertEquals(endedAt, dto.getEndedAt());
        assertEquals(BigDecimal.valueOf(3.5), dto.getHoursSpent());
        assertEquals("Implement the user authentication module with JWT", dto.getPromptUsed());
        assertEquals("Created JwtService, SecurityConfig, and AuthController", dto.getResponseSummary());
        assertEquals("Used Spring Security 6.x patterns", dto.getNotes());
    }

    @Test
    void toDto_shouldMapTaskIdFromNestedTask() {
        Task task = Task.builder().id(99L).build();

        TaskExecution execution = TaskExecution.builder()
                .id(5L)
                .task(task)
                .startedAt(LocalDateTime.now())
                .build();

        TaskExecutionDTO dto = mapper.toDto(execution);

        assertNotNull(dto);
        assertEquals(99L, dto.getTaskId());
    }

    @Test
    void toDto_shouldHandleNullTaskGracefully() {
        TaskExecution execution = TaskExecution.builder()
                .id(1L)
                .task(null)
                .startedAt(LocalDateTime.of(2026, 2, 10, 9, 0, 0))
                .hoursSpent(BigDecimal.valueOf(2.0))
                .build();

        TaskExecutionDTO dto = mapper.toDto(execution);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getTaskId());
        assertEquals(BigDecimal.valueOf(2.0), dto.getHoursSpent());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        TaskExecutionDTO dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toDto_shouldHandleNullOptionalFields() {
        Task task = Task.builder().id(1L).build();

        TaskExecution execution = TaskExecution.builder()
                .id(1L)
                .task(task)
                .startedAt(LocalDateTime.of(2026, 2, 10, 9, 0, 0))
                .build();
        // endedAt, hoursSpent, promptUsed, responseSummary, notes, createdAt are null

        TaskExecutionDTO dto = mapper.toDto(execution);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getTaskId());
        assertNotNull(dto.getStartedAt());
        assertNull(dto.getEndedAt());
        assertNull(dto.getHoursSpent());
        assertNull(dto.getPromptUsed());
        assertNull(dto.getResponseSummary());
        assertNull(dto.getNotes());
    }

    @Test
    void toDtoList_shouldMapAllElementsInList() {
        Task task1 = Task.builder().id(10L).build();
        Task task2 = Task.builder().id(20L).build();

        TaskExecution exec1 = TaskExecution.builder()
                .id(1L)
                .task(task1)
                .startedAt(LocalDateTime.of(2026, 2, 10, 9, 0, 0))
                .hoursSpent(BigDecimal.valueOf(3.0))
                .notes("First execution")
                .build();

        TaskExecution exec2 = TaskExecution.builder()
                .id(2L)
                .task(task2)
                .startedAt(LocalDateTime.of(2026, 2, 11, 9, 0, 0))
                .hoursSpent(BigDecimal.valueOf(4.0))
                .notes("Second execution")
                .build();

        List<TaskExecutionDTO> dtos = mapper.toDtoList(Arrays.asList(exec1, exec2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(10L, dtos.get(0).getTaskId());
        assertEquals(BigDecimal.valueOf(3.0), dtos.get(0).getHoursSpent());
        assertEquals("First execution", dtos.get(0).getNotes());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(20L, dtos.get(1).getTaskId());
        assertEquals(BigDecimal.valueOf(4.0), dtos.get(1).getHoursSpent());
        assertEquals("Second execution", dtos.get(1).getNotes());
    }

    @Test
    void toDtoList_shouldReturnEmptyListForEmptyInput() {
        List<TaskExecutionDTO> dtos = mapper.toDtoList(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullForNullInput() {
        List<TaskExecutionDTO> dtos = mapper.toDtoList(null);

        assertNull(dtos);
    }

    @Test
    void toDto_shouldMapExecutionWithLongPromptText() {
        Task task = Task.builder().id(1L).build();
        String longPrompt = "A".repeat(5000);
        String longSummary = "B".repeat(2000);

        TaskExecution execution = TaskExecution.builder()
                .id(1L)
                .task(task)
                .startedAt(LocalDateTime.of(2026, 2, 10, 9, 0, 0))
                .endedAt(LocalDateTime.of(2026, 2, 10, 12, 0, 0))
                .hoursSpent(BigDecimal.valueOf(3.0))
                .promptUsed(longPrompt)
                .responseSummary(longSummary)
                .build();

        TaskExecutionDTO dto = mapper.toDto(execution);

        assertNotNull(dto);
        assertEquals(longPrompt, dto.getPromptUsed());
        assertEquals(longSummary, dto.getResponseSummary());
    }
}
