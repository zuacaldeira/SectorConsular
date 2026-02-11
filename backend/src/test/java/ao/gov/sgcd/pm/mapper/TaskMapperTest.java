package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.TaskDTO;
import ao.gov.sgcd.pm.dto.TaskExecutionDTO;
import ao.gov.sgcd.pm.dto.TaskNoteDTO;
import ao.gov.sgcd.pm.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    /**
     * Tests for the jsonToList default method. Since this is a default method on the
     * interface, we can test it by creating an anonymous implementation that delegates
     * the abstract methods. This avoids needing Spring context for these tests.
     */
    @Nested
    class JsonToListTests {

        private TaskMapper mapper;

        @BeforeEach
        void setUp() {
            // Create a minimal implementation to test the default method
            mapper = new TaskMapper() {
                @Override
                public TaskDTO toDto(Task task) {
                    return null;
                }

                @Override
                public List<TaskDTO> toDtoList(List<Task> tasks) {
                    return null;
                }
            };
        }

        @Test
        void jsonToList_shouldParseValidJsonArray() {
            String json = "[\"file1.java\",\"file2.java\",\"file3.java\"]";

            List<String> result = mapper.jsonToList(json);

            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("file1.java", result.get(0));
            assertEquals("file2.java", result.get(1));
            assertEquals("file3.java", result.get(2));
        }

        @Test
        void jsonToList_shouldReturnEmptyListForNull() {
            List<String> result = mapper.jsonToList(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void jsonToList_shouldReturnEmptyListForBlankString() {
            List<String> result = mapper.jsonToList("");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void jsonToList_shouldReturnEmptyListForWhitespaceOnly() {
            List<String> result = mapper.jsonToList("   ");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void jsonToList_shouldReturnEmptyListForInvalidJson() {
            List<String> result = mapper.jsonToList("not a json array");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void jsonToList_shouldReturnEmptyListForMalformedJsonArray() {
            List<String> result = mapper.jsonToList("[\"unclosed");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void jsonToList_shouldHandleEmptyJsonArray() {
            List<String> result = mapper.jsonToList("[]");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void jsonToList_shouldHandleSingleElementArray() {
            List<String> result = mapper.jsonToList("[\"only-one\"]");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("only-one", result.get(0));
        }

        @Test
        void jsonToList_shouldHandleJsonWithSpecialCharacters() {
            String json = "[\"Criar ficheiro README.md\",\"Configuração do application.yml\",\"Teste com ação\"]";

            List<String> result = mapper.jsonToList(json);

            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("Criar ficheiro README.md", result.get(0));
            assertEquals("Configuração do application.yml", result.get(1));
            assertEquals("Teste com ação", result.get(2));
        }

        @Test
        void jsonToList_shouldReturnEmptyListForJsonObject() {
            List<String> result = mapper.jsonToList("{\"key\": \"value\"}");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    /**
     * Integration tests for the full TaskMapper mapping using Spring context.
     * This is necessary because TaskMapper uses TaskNoteMapper and TaskExecutionMapper
     * as sub-mappers via @Autowired injection in the generated implementation.
     */
    @Nested
    @SpringBootTest
    @ActiveProfiles("test")
    class MappingIntegrationTests {

        @Autowired
        private TaskMapper mapper;

        @Test
        void toDto_shouldMapAllDirectFields() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1 - Fundacao")
                    .nameEn("Sprint 1 - Foundation")
                    .weeks(4)
                    .totalHours(120)
                    .totalSessions(36)
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .status(SprintStatus.ACTIVE)
                    .build();

            LocalDateTime startedAt = LocalDateTime.of(2026, 1, 5, 9, 0, 0);
            LocalDateTime completedAt = LocalDateTime.of(2026, 1, 5, 12, 30, 0);

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Configuracao Inicial do Projecto")
                    .titleEn("Initial Project Setup")
                    .description("Setup the initial project structure")
                    .deliverables("[\"pom.xml\",\"application.yml\",\"docker-compose.yml\"]")
                    .validationCriteria("[\"mvn compile sem erros\",\"docker-compose up funcional\"]")
                    .coverageTarget("80%")
                    .status(TaskStatus.COMPLETED)
                    .actualHours(BigDecimal.valueOf(3.0))
                    .startedAt(startedAt)
                    .completedAt(completedAt)
                    .completionNotes("All setup tasks completed")
                    .blockers(null)
                    .sortOrder(1)
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertEquals(1L, dto.getId());
            assertEquals("S1-001", dto.getTaskCode());
            assertEquals(LocalDate.of(2026, 1, 5), dto.getSessionDate());
            assertEquals("MON", dto.getDayOfWeek());
            assertEquals(1, dto.getWeekNumber());
            assertEquals(BigDecimal.valueOf(3.5), dto.getPlannedHours());
            assertEquals("Configuracao Inicial do Projecto", dto.getTitle());
            assertEquals("Initial Project Setup", dto.getTitleEn());
            assertEquals("Setup the initial project structure", dto.getDescription());
            assertEquals("80%", dto.getCoverageTarget());
            assertEquals(TaskStatus.COMPLETED, dto.getStatus());
            assertEquals(BigDecimal.valueOf(3.0), dto.getActualHours());
            assertEquals(startedAt, dto.getStartedAt());
            assertEquals(completedAt, dto.getCompletedAt());
            assertEquals("All setup tasks completed", dto.getCompletionNotes());
            assertNull(dto.getBlockers());
            assertEquals(1, dto.getSortOrder());
        }

        @Test
        void toDto_shouldMapSprintIdFromNestedSprint() {
            Sprint sprint = Sprint.builder()
                    .id(2L)
                    .sprintNumber(2)
                    .name("Sprint 2 - Core")
                    .startDate(LocalDate.of(2026, 2, 3))
                    .endDate(LocalDate.of(2026, 2, 22))
                    .build();

            Task task = Task.builder()
                    .id(50L)
                    .sprint(sprint)
                    .taskCode("S2-001")
                    .sessionDate(LocalDate.of(2026, 2, 3))
                    .dayOfWeek("TUE")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Tarefa de Core")
                    .status(TaskStatus.PLANNED)
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertEquals(2L, dto.getSprintId());
            assertEquals(2, dto.getSprintNumber());
            assertEquals("Sprint 2 - Core", dto.getSprintName());
        }

        @Test
        void toDto_shouldMapDeliverablesFromJsonToList() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task with deliverables")
                    .status(TaskStatus.PLANNED)
                    .deliverables("[\"pom.xml\",\"Dockerfile\",\"docker-compose.yml\"]")
                    .validationCriteria("[\"mvn compile OK\",\"docker build OK\"]")
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNotNull(dto.getDeliverables());
            assertEquals(3, dto.getDeliverables().size());
            assertEquals("pom.xml", dto.getDeliverables().get(0));
            assertEquals("Dockerfile", dto.getDeliverables().get(1));
            assertEquals("docker-compose.yml", dto.getDeliverables().get(2));
        }

        @Test
        void toDto_shouldMapValidationCriteriaFromJsonToList() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task with criteria")
                    .status(TaskStatus.PLANNED)
                    .deliverables("[]")
                    .validationCriteria("[\"Testes unitarios passam\",\"Coverage >= 80%\"]")
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNotNull(dto.getValidationCriteria());
            assertEquals(2, dto.getValidationCriteria().size());
            assertEquals("Testes unitarios passam", dto.getValidationCriteria().get(0));
            assertEquals("Coverage >= 80%", dto.getValidationCriteria().get(1));
        }

        @Test
        void toDto_shouldReturnEmptyListForNullDeliverables() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task without deliverables")
                    .status(TaskStatus.PLANNED)
                    .deliverables(null)
                    .validationCriteria(null)
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNotNull(dto.getDeliverables());
            assertTrue(dto.getDeliverables().isEmpty());
            assertNotNull(dto.getValidationCriteria());
            assertTrue(dto.getValidationCriteria().isEmpty());
        }

        @Test
        void toDto_shouldMapNotesUsingSubMapper() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task with notes")
                    .status(TaskStatus.IN_PROGRESS)
                    .deliverables("[]")
                    .validationCriteria("[]")
                    .executions(new ArrayList<>())
                    .build();

            TaskNote note1 = TaskNote.builder()
                    .id(1L)
                    .task(task)
                    .noteType(NoteType.INFO)
                    .content("Started implementation")
                    .author("developer")
                    .createdAt(LocalDateTime.of(2026, 1, 5, 9, 0, 0))
                    .build();

            TaskNote note2 = TaskNote.builder()
                    .id(2L)
                    .task(task)
                    .noteType(NoteType.WARNING)
                    .content("External API dependency")
                    .author("developer")
                    .createdAt(LocalDateTime.of(2026, 1, 5, 10, 0, 0))
                    .build();

            task.setNotes(Arrays.asList(note1, note2));

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNotNull(dto.getNotes());
            assertEquals(2, dto.getNotes().size());

            TaskNoteDTO noteDto1 = dto.getNotes().get(0);
            assertEquals(1L, noteDto1.getId());
            assertEquals(1L, noteDto1.getTaskId());
            assertEquals(NoteType.INFO, noteDto1.getNoteType());
            assertEquals("Started implementation", noteDto1.getContent());

            TaskNoteDTO noteDto2 = dto.getNotes().get(1);
            assertEquals(2L, noteDto2.getId());
            assertEquals(NoteType.WARNING, noteDto2.getNoteType());
        }

        @Test
        void toDto_shouldMapExecutionsUsingSubMapper() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task with executions")
                    .status(TaskStatus.COMPLETED)
                    .deliverables("[]")
                    .validationCriteria("[]")
                    .notes(new ArrayList<>())
                    .build();

            TaskExecution exec1 = TaskExecution.builder()
                    .id(1L)
                    .task(task)
                    .startedAt(LocalDateTime.of(2026, 1, 5, 9, 0, 0))
                    .endedAt(LocalDateTime.of(2026, 1, 5, 12, 0, 0))
                    .hoursSpent(BigDecimal.valueOf(3.0))
                    .promptUsed("Implement entity classes")
                    .responseSummary("Created Sprint and Task entities")
                    .notes("First session")
                    .build();

            task.setExecutions(List.of(exec1));

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNotNull(dto.getExecutions());
            assertEquals(1, dto.getExecutions().size());

            TaskExecutionDTO execDto = dto.getExecutions().get(0);
            assertEquals(1L, execDto.getId());
            assertEquals(1L, execDto.getTaskId());
            assertEquals(BigDecimal.valueOf(3.0), execDto.getHoursSpent());
            assertEquals("Implement entity classes", execDto.getPromptUsed());
        }

        @Test
        void toDto_shouldHandleNullSprintGracefully() {
            Task task = Task.builder()
                    .id(1L)
                    .sprint(null)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task without sprint")
                    .status(TaskStatus.PLANNED)
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNull(dto.getSprintId());
            assertNull(dto.getSprintNumber());
            assertNull(dto.getSprintName());
        }

        @Test
        void toDto_shouldReturnNullForNullInput() {
            TaskDTO dto = mapper.toDto(null);

            assertNull(dto);
        }

        @Test
        void toDtoList_shouldMapAllElementsInList() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task1 = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("First Task")
                    .status(TaskStatus.COMPLETED)
                    .deliverables("[\"file1.java\"]")
                    .validationCriteria("[\"test passes\"]")
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            Task task2 = Task.builder()
                    .id(2L)
                    .sprint(sprint)
                    .taskCode("S1-002")
                    .sessionDate(LocalDate.of(2026, 1, 6))
                    .dayOfWeek("TUE")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Second Task")
                    .status(TaskStatus.PLANNED)
                    .deliverables("[\"file2.java\"]")
                    .validationCriteria("[\"test passes\"]")
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            List<TaskDTO> dtos = mapper.toDtoList(Arrays.asList(task1, task2));

            assertNotNull(dtos);
            assertEquals(2, dtos.size());
            assertEquals("S1-001", dtos.get(0).getTaskCode());
            assertEquals("First Task", dtos.get(0).getTitle());
            assertEquals(1L, dtos.get(0).getSprintId());
            assertEquals("S1-002", dtos.get(1).getTaskCode());
            assertEquals("Second Task", dtos.get(1).getTitle());
        }

        @Test
        void toDtoList_shouldReturnEmptyListForEmptyInput() {
            List<TaskDTO> dtos = mapper.toDtoList(Collections.emptyList());

            assertNotNull(dtos);
            assertTrue(dtos.isEmpty());
        }

        @Test
        void toDtoList_shouldReturnNullForNullInput() {
            List<TaskDTO> dtos = mapper.toDtoList(null);

            assertNull(dtos);
        }

        @Test
        void toDto_shouldMapAllTaskStatuses() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            for (TaskStatus status : TaskStatus.values()) {
                Task task = Task.builder()
                        .id(1L)
                        .sprint(sprint)
                        .taskCode("S1-001")
                        .sessionDate(LocalDate.of(2026, 1, 5))
                        .dayOfWeek("MON")
                        .weekNumber(1)
                        .plannedHours(BigDecimal.valueOf(3.5))
                        .title("Task with status " + status)
                        .status(status)
                        .deliverables("[]")
                        .validationCriteria("[]")
                        .notes(new ArrayList<>())
                        .executions(new ArrayList<>())
                        .build();

                TaskDTO dto = mapper.toDto(task);

                assertNotNull(dto);
                assertEquals(status, dto.getStatus());
            }
        }

        @Test
        void toDto_shouldHandleEmptyNotesAndExecutions() {
            Sprint sprint = Sprint.builder()
                    .id(1L)
                    .sprintNumber(1)
                    .name("Sprint 1")
                    .startDate(LocalDate.of(2026, 1, 5))
                    .endDate(LocalDate.of(2026, 2, 1))
                    .build();

            Task task = Task.builder()
                    .id(1L)
                    .sprint(sprint)
                    .taskCode("S1-001")
                    .sessionDate(LocalDate.of(2026, 1, 5))
                    .dayOfWeek("MON")
                    .weekNumber(1)
                    .plannedHours(BigDecimal.valueOf(3.5))
                    .title("Task with empty collections")
                    .status(TaskStatus.PLANNED)
                    .deliverables("[]")
                    .validationCriteria("[]")
                    .notes(new ArrayList<>())
                    .executions(new ArrayList<>())
                    .build();

            TaskDTO dto = mapper.toDto(task);

            assertNotNull(dto);
            assertNotNull(dto.getNotes());
            assertTrue(dto.getNotes().isEmpty());
            assertNotNull(dto.getExecutions());
            assertTrue(dto.getExecutions().isEmpty());
        }
    }
}
