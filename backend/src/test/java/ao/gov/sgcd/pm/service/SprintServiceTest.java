package ao.gov.sgcd.pm.service;

import ao.gov.sgcd.pm.dto.SprintDTO;
import ao.gov.sgcd.pm.dto.SprintProgressDTO;
import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import ao.gov.sgcd.pm.entity.TaskStatus;
import ao.gov.sgcd.pm.mapper.SprintMapper;
import ao.gov.sgcd.pm.repository.SprintRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SprintMapper sprintMapper;

    @InjectMocks
    private SprintService sprintService;

    // --- Helper builders ---

    private Sprint buildSprint(Long id, int number, String name, SprintStatus status,
                               int totalSessions, int completedSessions) {
        return Sprint.builder()
                .id(id)
                .sprintNumber(number)
                .name(name)
                .nameEn(name + " EN")
                .status(status)
                .totalSessions(totalSessions)
                .completedSessions(completedSessions)
                .totalHours(totalSessions * 3)
                .actualHours(BigDecimal.valueOf(completedSessions * 3))
                .weeks(4)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 4, 12))
                .color("#CC092F")
                .build();
    }

    private SprintDTO buildSprintDto(Long id, int number, String name, SprintStatus status) {
        return SprintDTO.builder()
                .id(id)
                .sprintNumber(number)
                .name(name)
                .nameEn(name + " EN")
                .status(status)
                .build();
    }

    // =====================================================================
    // findAll
    // =====================================================================

    @Test
    void findAll_shouldReturnEnrichedDtos() {
        // given
        Sprint sprint1 = buildSprint(1L, 1, "Fundacao", SprintStatus.COMPLETED, 36, 36);
        Sprint sprint2 = buildSprint(2L, 2, "Backend Core", SprintStatus.ACTIVE, 40, 10);

        when(sprintRepository.findAllOrdered()).thenReturn(List.of(sprint1, sprint2));

        SprintDTO dto1 = buildSprintDto(1L, 1, "Fundacao", SprintStatus.COMPLETED);
        SprintDTO dto2 = buildSprintDto(2L, 2, "Backend Core", SprintStatus.ACTIVE);
        when(sprintMapper.toDto(sprint1)).thenReturn(dto1);
        when(sprintMapper.toDto(sprint2)).thenReturn(dto2);

        when(taskRepository.findBySprintIdOrderBySortOrderAsc(1L)).thenReturn(Collections.nCopies(36, null));
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(2L)).thenReturn(Collections.nCopies(40, null));

        // when
        List<SprintDTO> result = sprintService.findAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(36, result.get(0).getTaskCount());
        assertEquals(100.0, result.get(0).getProgressPercent());

        assertEquals(40, result.get(1).getTaskCount());
        assertEquals(25.0, result.get(1).getProgressPercent());

        verify(sprintRepository).findAllOrdered();
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoSprints() {
        // given
        when(sprintRepository.findAllOrdered()).thenReturn(Collections.emptyList());

        // when
        List<SprintDTO> result = sprintService.findAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =====================================================================
    // findById
    // =====================================================================

    @Test
    void findById_shouldReturnEnrichedDto() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", SprintStatus.ACTIVE, 36, 12);
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        SprintDTO dto = buildSprintDto(1L, 1, "Fundacao", SprintStatus.ACTIVE);
        when(sprintMapper.toDto(sprint)).thenReturn(dto);
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(1L)).thenReturn(Collections.nCopies(36, null));

        // when
        SprintDTO result = sprintService.findById(1L);

        // then
        assertNotNull(result);
        assertEquals(36, result.getTaskCount());
        double expectedProgress = (12 * 100.0) / 36;
        assertEquals(expectedProgress, result.getProgressPercent(), 0.01);
        verify(sprintRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        // given
        when(sprintRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sprintService.findById(99L));
        assertTrue(ex.getMessage().contains("Sprint não encontrado"));
        assertTrue(ex.getMessage().contains("99"));
    }

    // =====================================================================
    // findActive
    // =====================================================================

    @Test
    void findActive_shouldReturnActiveSprint() {
        // given
        Sprint active = buildSprint(2L, 2, "Backend Core", SprintStatus.ACTIVE, 40, 15);
        when(sprintRepository.findActiveSprint()).thenReturn(Optional.of(active));

        SprintDTO dto = buildSprintDto(2L, 2, "Backend Core", SprintStatus.ACTIVE);
        when(sprintMapper.toDto(active)).thenReturn(dto);
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(2L)).thenReturn(Collections.nCopies(40, null));

        // when
        SprintDTO result = sprintService.findActive();

        // then
        assertNotNull(result);
        assertEquals(2, result.getSprintNumber());
        verify(sprintRepository).findActiveSprint();
        verify(sprintRepository, never()).findFirstByStatusOrderBySprintNumberAsc(any());
    }

    @Test
    void findActive_shouldFallBackToFirstPlannedWhenNoActive() {
        // given
        when(sprintRepository.findActiveSprint()).thenReturn(Optional.empty());

        Sprint planned = buildSprint(3L, 3, "Frontend", SprintStatus.PLANNED, 30, 0);
        when(sprintRepository.findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED))
                .thenReturn(Optional.of(planned));

        SprintDTO dto = buildSprintDto(3L, 3, "Frontend", SprintStatus.PLANNED);
        when(sprintMapper.toDto(planned)).thenReturn(dto);
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(3L)).thenReturn(Collections.nCopies(30, null));

        // when
        SprintDTO result = sprintService.findActive();

        // then
        assertNotNull(result);
        assertEquals(3, result.getSprintNumber());
        verify(sprintRepository).findActiveSprint();
        verify(sprintRepository).findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED);
    }

    @Test
    void findActive_shouldThrowWhenNoActiveOrPlanned() {
        // given
        when(sprintRepository.findActiveSprint()).thenReturn(Optional.empty());
        when(sprintRepository.findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED))
                .thenReturn(Optional.empty());

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sprintService.findActive());
        assertTrue(ex.getMessage().contains("Nenhum sprint activo ou planeado"));
    }

    // =====================================================================
    // getProgress
    // =====================================================================

    @Test
    void getProgress_shouldCalculateAllMetrics() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", SprintStatus.ACTIVE, 36, 20);
        sprint.setTotalHours(108);
        sprint.setActualHours(BigDecimal.valueOf(60));

        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(taskRepository.countBySprintIdAndStatus(1L, TaskStatus.PLANNED)).thenReturn(10);
        when(taskRepository.countBySprintIdAndStatus(1L, TaskStatus.IN_PROGRESS)).thenReturn(2);
        when(taskRepository.countBySprintIdAndStatus(1L, TaskStatus.COMPLETED)).thenReturn(20);
        when(taskRepository.countBySprintIdAndStatus(1L, TaskStatus.BLOCKED)).thenReturn(3);
        when(taskRepository.countBySprintIdAndStatus(1L, TaskStatus.SKIPPED)).thenReturn(1);

        // when
        SprintProgressDTO result = sprintService.getProgress(1L);

        // then
        assertNotNull(result);
        assertEquals(1, result.getSprintNumber());
        assertEquals("Fundacao", result.getName());
        assertEquals(36, result.getTotalSessions());
        assertEquals(20, result.getCompletedSessions());
        assertEquals(108, result.getTotalHours());
        assertEquals(BigDecimal.valueOf(60), result.getActualHours());

        double expectedProgress = (20 * 100.0) / 36;
        assertEquals(expectedProgress, result.getProgressPercent(), 0.01);

        assertEquals(10, result.getPlannedTasks());
        assertEquals(2, result.getInProgressTasks());
        assertEquals(20, result.getCompletedTasks());
        assertEquals(3, result.getBlockedTasks());
        assertEquals(1, result.getSkippedTasks());
    }

    @Test
    void getProgress_shouldReturnZeroProgressWhenTotalSessionsIsZero() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", SprintStatus.PLANNED, 0, 0);
        sprint.setTotalHours(0);

        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(taskRepository.countBySprintIdAndStatus(eq(1L), any())).thenReturn(0);

        // when
        SprintProgressDTO result = sprintService.getProgress(1L);

        // then
        assertEquals(0.0, result.getProgressPercent());
    }

    @Test
    void getProgress_shouldThrowWhenSprintNotFound() {
        // given
        when(sprintRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sprintService.getProgress(99L));
        assertTrue(ex.getMessage().contains("Sprint não encontrado"));
    }

    // =====================================================================
    // update
    // =====================================================================

    @Test
    void update_shouldUpdateStatusAndCompletionNotes() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", SprintStatus.ACTIVE, 36, 36);
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        SprintDTO inputDto = SprintDTO.builder()
                .status(SprintStatus.COMPLETED)
                .completionNotes("Sprint concluido com sucesso")
                .build();

        SprintDTO mappedDto = buildSprintDto(1L, 1, "Fundacao", SprintStatus.COMPLETED);
        when(sprintMapper.toDto(any(Sprint.class))).thenReturn(mappedDto);
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(1L)).thenReturn(Collections.nCopies(36, null));

        // when
        SprintDTO result = sprintService.update(1L, inputDto);

        // then
        assertNotNull(result);
        verify(sprintRepository).save(sprint);
        assertEquals(SprintStatus.COMPLETED, sprint.getStatus());
        assertEquals("Sprint concluido com sucesso", sprint.getCompletionNotes());
    }

    @Test
    void update_shouldSkipNullFields() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", SprintStatus.ACTIVE, 36, 10);
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        SprintDTO inputDto = SprintDTO.builder().build(); // all null

        SprintDTO mappedDto = buildSprintDto(1L, 1, "Fundacao", SprintStatus.ACTIVE);
        when(sprintMapper.toDto(any(Sprint.class))).thenReturn(mappedDto);
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(1L)).thenReturn(Collections.nCopies(36, null));

        // when
        SprintDTO result = sprintService.update(1L, inputDto);

        // then
        assertNotNull(result);
        assertEquals(SprintStatus.ACTIVE, sprint.getStatus()); // unchanged
    }

    @Test
    void update_shouldThrowWhenSprintNotFound() {
        // given
        when(sprintRepository.findById(99L)).thenReturn(Optional.empty());

        SprintDTO inputDto = SprintDTO.builder().status(SprintStatus.COMPLETED).build();

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sprintService.update(99L, inputDto));
        assertTrue(ex.getMessage().contains("Sprint não encontrado"));
    }

    // =====================================================================
    // enrichDto (tested indirectly)
    // =====================================================================

    @Test
    void enrichDto_shouldSetZeroProgressWhenTotalSessionsIsZero() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", SprintStatus.PLANNED, 0, 0);
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        SprintDTO dto = buildSprintDto(1L, 1, "Fundacao", SprintStatus.PLANNED);
        when(sprintMapper.toDto(sprint)).thenReturn(dto);
        when(taskRepository.findBySprintIdOrderBySortOrderAsc(1L)).thenReturn(Collections.emptyList());

        // when
        SprintDTO result = sprintService.findById(1L);

        // then
        assertEquals(0, result.getTaskCount());
        assertEquals(0.0, result.getProgressPercent());
    }
}
