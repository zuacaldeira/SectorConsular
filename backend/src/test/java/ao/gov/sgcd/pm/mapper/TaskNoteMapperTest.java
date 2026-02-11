package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.TaskNoteDTO;
import ao.gov.sgcd.pm.entity.NoteType;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskNote;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskNoteMapperTest {

    private final TaskNoteMapper mapper = Mappers.getMapper(TaskNoteMapper.class);

    @Test
    void toDto_shouldMapAllFieldsIncludingTaskId() {
        Task task = Task.builder().id(10L).build();
        LocalDateTime createdAt = LocalDateTime.of(2026, 2, 10, 14, 30, 0);

        TaskNote note = TaskNote.builder()
                .id(1L)
                .task(task)
                .noteType(NoteType.WARNING)
                .content("This task has a dependency on Sprint 1 completion")
                .author("developer")
                .createdAt(createdAt)
                .build();

        TaskNoteDTO dto = mapper.toDto(note);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getTaskId());
        assertEquals(NoteType.WARNING, dto.getNoteType());
        assertEquals("This task has a dependency on Sprint 1 completion", dto.getContent());
        assertEquals("developer", dto.getAuthor());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void toDto_shouldMapTaskIdFromNestedTask() {
        Task task = Task.builder().id(42L).build();

        TaskNote note = TaskNote.builder()
                .id(5L)
                .task(task)
                .noteType(NoteType.INFO)
                .content("General note")
                .build();

        TaskNoteDTO dto = mapper.toDto(note);

        assertNotNull(dto);
        assertEquals(42L, dto.getTaskId());
    }

    @Test
    void toDto_shouldHandleNullTaskGracefully() {
        TaskNote note = TaskNote.builder()
                .id(1L)
                .task(null)
                .noteType(NoteType.BLOCKER)
                .content("Blocked note")
                .author("developer")
                .build();

        TaskNoteDTO dto = mapper.toDto(note);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getTaskId());
        assertEquals(NoteType.BLOCKER, dto.getNoteType());
        assertEquals("Blocked note", dto.getContent());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        TaskNoteDTO dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toDto_shouldMapAllNoteTypes() {
        Task task = Task.builder().id(1L).build();

        for (NoteType noteType : NoteType.values()) {
            TaskNote note = TaskNote.builder()
                    .id(1L)
                    .task(task)
                    .noteType(noteType)
                    .content("Content for " + noteType.name())
                    .build();

            TaskNoteDTO dto = mapper.toDto(note);

            assertNotNull(dto);
            assertEquals(noteType, dto.getNoteType());
        }
    }

    @Test
    void toDtoList_shouldMapAllElementsInList() {
        Task task1 = Task.builder().id(10L).build();
        Task task2 = Task.builder().id(20L).build();

        TaskNote note1 = TaskNote.builder()
                .id(1L)
                .task(task1)
                .noteType(NoteType.INFO)
                .content("First note")
                .author("developer")
                .build();

        TaskNote note2 = TaskNote.builder()
                .id(2L)
                .task(task2)
                .noteType(NoteType.DECISION)
                .content("Second note")
                .author("admin")
                .build();

        List<TaskNoteDTO> dtos = mapper.toDtoList(Arrays.asList(note1, note2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(10L, dtos.get(0).getTaskId());
        assertEquals("First note", dtos.get(0).getContent());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(20L, dtos.get(1).getTaskId());
        assertEquals("Second note", dtos.get(1).getContent());
    }

    @Test
    void toDtoList_shouldReturnEmptyListForEmptyInput() {
        List<TaskNoteDTO> dtos = mapper.toDtoList(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullForNullInput() {
        List<TaskNoteDTO> dtos = mapper.toDtoList(null);

        assertNull(dtos);
    }

    @Test
    void toDto_shouldHandleNullOptionalFields() {
        Task task = Task.builder().id(1L).build();

        TaskNote note = TaskNote.builder()
                .id(1L)
                .task(task)
                .content("Minimal note")
                .build();
        // noteType and author are null

        TaskNoteDTO dto = mapper.toDto(note);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getTaskId());
        assertNull(dto.getNoteType());
        assertEquals("Minimal note", dto.getContent());
        assertNull(dto.getAuthor());
        assertNull(dto.getCreatedAt());
    }
}
