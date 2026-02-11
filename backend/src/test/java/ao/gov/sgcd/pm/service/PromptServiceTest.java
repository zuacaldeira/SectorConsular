package ao.gov.sgcd.pm.service;

import ao.gov.sgcd.pm.dto.PromptDTO;
import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskStatus;
import ao.gov.sgcd.pm.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private PromptService promptService;

    // --- Helper builders ---

    private Sprint buildSprint(Long id, int number, String name, int totalSessions, int completedSessions) {
        return Sprint.builder()
                .id(id)
                .sprintNumber(number)
                .name(name)
                .nameEn(name + " EN")
                .status(SprintStatus.ACTIVE)
                .totalSessions(totalSessions)
                .completedSessions(completedSessions)
                .totalHours(totalSessions * 3)
                .actualHours(BigDecimal.valueOf(completedSessions * 3))
                .weeks(4)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 4, 12))
                .focus("Backend & Infrastructure")
                .color("#CC092F")
                .build();
    }

    private Task buildTask(Long id, String taskCode, Sprint sprint) {
        return Task.builder()
                .id(id)
                .taskCode(taskCode)
                .title("Configuracao do ambiente de desenvolvimento")
                .description("Configurar Docker, Maven e dependencias base")
                .sprint(sprint)
                .status(TaskStatus.PLANNED)
                .sessionDate(LocalDate.of(2026, 3, 10))
                .dayOfWeek("TER")
                .weekNumber(2)
                .plannedHours(BigDecimal.valueOf(3.5))
                .deliverables("[\"docker-compose.yml\",\"pom.xml\"]")
                .validationCriteria("[\"Docker containers activos\",\"Build Maven ok\"]")
                .coverageTarget("80%")
                .sortOrder(1)
                .notes(new ArrayList<>())
                .executions(new ArrayList<>())
                .build();
    }

    // =====================================================================
    // getPromptForTask
    // =====================================================================

    @Test
    void getPromptForTask_shouldBuildPromptWithAllDetails() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 5);
        Task task = buildTask(10L, "S1-10", sprint);

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(9);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getPromptForTask(10L);

        // then
        assertNotNull(result);
        assertEquals(10L, result.getTaskId());
        assertEquals("S1-10", result.getTaskCode());
        assertEquals("Configuracao do ambiente de desenvolvimento", result.getTitle());

        assertNotNull(result.getPrompt());
        assertTrue(result.getPrompt().contains("Sprint 1"));
        assertTrue(result.getPrompt().contains("Fundacao"));
        assertTrue(result.getPrompt().contains("S1-10"));
        assertTrue(result.getPrompt().contains("10/03/2026"));
        assertTrue(result.getPrompt().contains("Session 10 of 204"));
        assertTrue(result.getPrompt().contains("3.5"));
        assertTrue(result.getPrompt().contains("Backend & Infrastructure"));
        assertTrue(result.getPrompt().contains("5/36"));
        assertTrue(result.getPrompt().contains("docker-compose.yml"));
        assertTrue(result.getPrompt().contains("Docker containers activos"));
        assertTrue(result.getPrompt().contains("80%"));
        assertTrue(result.getPrompt().contains("Production-quality code only"));
    }

    @Test
    void getPromptForTask_shouldIncludeRecentCompletedTasks() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 5);
        Task task = buildTask(6L, "S1-06", sprint);

        Task recentTask = Task.builder()
                .id(5L).taskCode("S1-05").title("Sessao anterior")
                .sprint(sprint).status(TaskStatus.COMPLETED)
                .sessionDate(LocalDate.of(2026, 3, 9)).dayOfWeek("SEG")
                .weekNumber(2).plannedHours(BigDecimal.valueOf(3.0))
                .build();

        when(taskRepository.findById(6L)).thenReturn(Optional.of(task));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(5);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(List.of(recentTask));

        // when
        PromptDTO result = promptService.getPromptForTask(6L);

        // then
        assertNotNull(result.getPrompt());
        assertTrue(result.getPrompt().contains("S1-05"));
        assertTrue(result.getPrompt().contains("Sessao anterior"));
    }

    @Test
    void getPromptForTask_shouldHandleNullDescriptionAndDeliverables() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 0);
        Task task = buildTask(1L, "S1-01", sprint);
        task.setDescription(null);
        task.setDeliverables(null);
        task.setValidationCriteria(null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(0);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getPromptForTask(1L);

        // then
        assertNotNull(result);
        assertNotNull(result.getPrompt());
        // Should not throw or contain "null" text
        assertFalse(result.getPrompt().contains("null"));
    }

    @Test
    void getPromptForTask_shouldThrowWhenTaskNotFound() {
        // given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> promptService.getPromptForTask(99L));
        assertTrue(ex.getMessage().contains("Tarefa não encontrada"));
    }

    @Test
    void getPromptForTask_shouldHandleZeroTotalSessions() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 0, 0);
        Task task = buildTask(1L, "S1-01", sprint);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(0);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getPromptForTask(1L);

        // then
        assertNotNull(result);
        // Should contain 0/0 (0%)
        assertTrue(result.getPrompt().contains("0/0"));
    }

    // =====================================================================
    // getTodayPrompt
    // =====================================================================

    @Test
    void getTodayPrompt_shouldBuildPromptForTodayTask() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 5);
        Task todayTask = buildTask(6L, "S1-06", sprint);
        todayTask.setSessionDate(LocalDate.now());

        when(taskRepository.findBySessionDate(LocalDate.now())).thenReturn(Optional.of(todayTask));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(5);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getTodayPrompt();

        // then
        assertNotNull(result);
        assertEquals(6L, result.getTaskId());
        assertEquals("S1-06", result.getTaskCode());
        assertNotNull(result.getPrompt());
    }

    @Test
    void getTodayPrompt_shouldFallBackToUpcomingPlanned() {
        // given
        when(taskRepository.findBySessionDate(any(LocalDate.class))).thenReturn(Optional.empty());

        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 5);
        Task upcoming = buildTask(7L, "S1-07", sprint);
        upcoming.setSessionDate(LocalDate.now().plusDays(1));

        when(taskRepository.findUpcomingPlanned(any(LocalDate.class))).thenReturn(List.of(upcoming));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(5);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getTodayPrompt();

        // then
        assertNotNull(result);
        assertEquals(7L, result.getTaskId());
        assertEquals("S1-07", result.getTaskCode());
    }

    @Test
    void getTodayPrompt_shouldReturnFallbackMessageWhenNoTasks() {
        // given
        when(taskRepository.findBySessionDate(any(LocalDate.class))).thenReturn(Optional.empty());
        when(taskRepository.findUpcomingPlanned(any(LocalDate.class))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getTodayPrompt();

        // then
        assertNotNull(result);
        assertNull(result.getTaskId());
        assertNull(result.getTaskCode());
        assertNull(result.getTitle());
        assertEquals("Nenhuma tarefa encontrada para hoje.", result.getPrompt());
    }

    // =====================================================================
    // getProjectContext
    // =====================================================================

    @Test
    void getProjectContext_shouldReturnStaticContextString() {
        // when
        String context = promptService.getProjectContext();

        // then
        assertNotNull(context);
        assertTrue(context.contains("SGCD"));
        assertTrue(context.contains("Spring Boot 3.x"));
        assertTrue(context.contains("Angular 17+"));
        assertTrue(context.contains("MySQL 8.0"));
        assertTrue(context.contains("Kafka"));
        assertTrue(context.contains("Keycloak"));
        assertTrue(context.contains("Hetzner Cloud"));
        assertTrue(context.contains("80%"));
        assertTrue(context.contains("MapStruct"));
        assertTrue(context.contains("Flyway"));
    }

    @Test
    void getProjectContext_shouldReturnConsistentValue() {
        // when
        String context1 = promptService.getProjectContext();
        String context2 = promptService.getProjectContext();

        // then
        assertEquals(context1, context2);
    }

    // =====================================================================
    // buildPrompt (tested indirectly via getPromptForTask)
    // =====================================================================

    @Test
    void buildPrompt_shouldHandleInvalidJsonDeliverables() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 0);
        Task task = buildTask(1L, "S1-01", sprint);
        task.setDeliverables("not valid json");
        task.setValidationCriteria("{ also invalid }");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(0);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getPromptForTask(1L);

        // then - should not throw, just return prompt without deliverables content
        assertNotNull(result);
        assertNotNull(result.getPrompt());
    }

    @Test
    void buildPrompt_shouldHandleEmptyJsonDeliverables() {
        // given
        Sprint sprint = buildSprint(1L, 1, "Fundacao", 36, 0);
        Task task = buildTask(1L, "S1-01", sprint);
        task.setDeliverables("");
        task.setValidationCriteria("   ");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.countByStatus(TaskStatus.COMPLETED)).thenReturn(0);
        when(taskRepository.findRecentCompleted(PageRequest.of(0, 3))).thenReturn(Collections.emptyList());

        // when
        PromptDTO result = promptService.getPromptForTask(1L);

        // then
        assertNotNull(result);
        assertNotNull(result.getPrompt());
    }
}
