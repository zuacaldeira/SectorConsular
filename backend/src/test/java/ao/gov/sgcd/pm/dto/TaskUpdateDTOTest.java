package ao.gov.sgcd.pm.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaskUpdateDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        TaskUpdateDTO dto = TaskUpdateDTO.builder()
                .completionNotes("Task completed successfully")
                .blockers("None")
                .actualHours(BigDecimal.valueOf(3.5))
                .description("Updated description")
                .build();

        assertEquals("Task completed successfully", dto.getCompletionNotes());
        assertEquals("None", dto.getBlockers());
        assertEquals(BigDecimal.valueOf(3.5), dto.getActualHours());
        assertEquals("Updated description", dto.getDescription());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        TaskUpdateDTO dto = new TaskUpdateDTO();

        assertNull(dto.getCompletionNotes());
        assertNull(dto.getBlockers());
        assertNull(dto.getActualHours());
        assertNull(dto.getDescription());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        TaskUpdateDTO dto = new TaskUpdateDTO(
                "Completion notes", "Blocked by API", BigDecimal.valueOf(2.0), "New description"
        );

        assertEquals("Completion notes", dto.getCompletionNotes());
        assertEquals("Blocked by API", dto.getBlockers());
        assertEquals(BigDecimal.valueOf(2.0), dto.getActualHours());
        assertEquals("New description", dto.getDescription());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        TaskUpdateDTO dto = new TaskUpdateDTO();

        dto.setCompletionNotes("Done");
        dto.setBlockers("No blockers");
        dto.setActualHours(BigDecimal.valueOf(4.0));
        dto.setDescription("Some description");

        assertEquals("Done", dto.getCompletionNotes());
        assertEquals("No blockers", dto.getBlockers());
        assertEquals(BigDecimal.valueOf(4.0), dto.getActualHours());
        assertEquals("Some description", dto.getDescription());
    }

    @Test
    void equals_reflexive() {
        TaskUpdateDTO dto = TaskUpdateDTO.builder().completionNotes("Notes").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        TaskUpdateDTO dto1 = TaskUpdateDTO.builder()
                .completionNotes("Notes")
                .actualHours(BigDecimal.valueOf(3.0))
                .build();
        TaskUpdateDTO dto2 = TaskUpdateDTO.builder()
                .completionNotes("Notes")
                .actualHours(BigDecimal.valueOf(3.0))
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        TaskUpdateDTO dto = TaskUpdateDTO.builder().completionNotes("Notes").build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        TaskUpdateDTO dto = TaskUpdateDTO.builder().completionNotes("Notes").build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        TaskUpdateDTO dto1 = TaskUpdateDTO.builder().completionNotes("Notes 1").build();
        TaskUpdateDTO dto2 = TaskUpdateDTO.builder().completionNotes("Notes 2").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        TaskUpdateDTO dto1 = TaskUpdateDTO.builder()
                .completionNotes("Notes")
                .blockers("None")
                .build();
        TaskUpdateDTO dto2 = TaskUpdateDTO.builder()
                .completionNotes("Notes")
                .blockers("None")
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        TaskUpdateDTO dto1 = TaskUpdateDTO.builder().completionNotes("Notes 1").build();
        TaskUpdateDTO dto2 = TaskUpdateDTO.builder().completionNotes("Notes 2").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        TaskUpdateDTO dto = TaskUpdateDTO.builder()
                .completionNotes("All done")
                .actualHours(BigDecimal.valueOf(3.5))
                .build();

        String result = dto.toString();
        assertTrue(result.contains("TaskUpdateDTO"));
        assertTrue(result.contains("All done"));
        assertTrue(result.contains("3.5"));
    }
}
