package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.NoteType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskNoteDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 6, 14, 30);

        TaskNoteDTO dto = TaskNoteDTO.builder()
                .id(1L)
                .taskId(10L)
                .noteType(NoteType.INFO)
                .content("Task started on time")
                .author("admin")
                .createdAt(createdAt)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getTaskId());
        assertEquals(NoteType.INFO, dto.getNoteType());
        assertEquals("Task started on time", dto.getContent());
        assertEquals("admin", dto.getAuthor());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        TaskNoteDTO dto = new TaskNoteDTO();

        assertNull(dto.getId());
        assertNull(dto.getTaskId());
        assertNull(dto.getNoteType());
        assertNull(dto.getContent());
        assertNull(dto.getAuthor());
        assertNull(dto.getCreatedAt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 2, 15, 10, 0);

        TaskNoteDTO dto = new TaskNoteDTO(
                2L, 20L, NoteType.WARNING, "Deadline approaching", "developer", createdAt
        );

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getTaskId());
        assertEquals(NoteType.WARNING, dto.getNoteType());
        assertEquals("Deadline approaching", dto.getContent());
        assertEquals("developer", dto.getAuthor());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        TaskNoteDTO dto = new TaskNoteDTO();
        LocalDateTime createdAt = LocalDateTime.of(2026, 3, 1, 8, 0);

        dto.setId(3L);
        dto.setTaskId(30L);
        dto.setNoteType(NoteType.BLOCKER);
        dto.setContent("Database unavailable");
        dto.setAuthor("admin");
        dto.setCreatedAt(createdAt);

        assertEquals(3L, dto.getId());
        assertEquals(30L, dto.getTaskId());
        assertEquals(NoteType.BLOCKER, dto.getNoteType());
        assertEquals("Database unavailable", dto.getContent());
        assertEquals("admin", dto.getAuthor());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkWithAllNoteTypes() {
        TaskNoteDTO dto = new TaskNoteDTO();

        dto.setNoteType(NoteType.DECISION);
        assertEquals(NoteType.DECISION, dto.getNoteType());

        dto.setNoteType(NoteType.OBSERVATION);
        assertEquals(NoteType.OBSERVATION, dto.getNoteType());
    }

    @Test
    void equals_reflexive() {
        TaskNoteDTO dto = TaskNoteDTO.builder().id(1L).content("Note").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 12, 0);
        TaskNoteDTO dto1 = TaskNoteDTO.builder().id(1L).taskId(10L).noteType(NoteType.INFO).content("Note").createdAt(createdAt).build();
        TaskNoteDTO dto2 = TaskNoteDTO.builder().id(1L).taskId(10L).noteType(NoteType.INFO).content("Note").createdAt(createdAt).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        TaskNoteDTO dto = TaskNoteDTO.builder().id(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        TaskNoteDTO dto = TaskNoteDTO.builder().id(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        TaskNoteDTO dto1 = TaskNoteDTO.builder().id(1L).content("Note 1").build();
        TaskNoteDTO dto2 = TaskNoteDTO.builder().id(2L).content("Note 2").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        TaskNoteDTO dto1 = TaskNoteDTO.builder().id(1L).taskId(10L).content("Note").build();
        TaskNoteDTO dto2 = TaskNoteDTO.builder().id(1L).taskId(10L).content("Note").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        TaskNoteDTO dto1 = TaskNoteDTO.builder().id(1L).content("Note 1").build();
        TaskNoteDTO dto2 = TaskNoteDTO.builder().id(2L).content("Note 2").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        TaskNoteDTO dto = TaskNoteDTO.builder()
                .id(1L)
                .noteType(NoteType.INFO)
                .content("Important note")
                .build();

        String result = dto.toString();
        assertTrue(result.contains("TaskNoteDTO"));
        assertTrue(result.contains("INFO"));
        assertTrue(result.contains("Important note"));
    }
}
