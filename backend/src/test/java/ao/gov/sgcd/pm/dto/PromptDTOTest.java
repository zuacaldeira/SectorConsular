package ao.gov.sgcd.pm.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromptDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        PromptDTO dto = PromptDTO.builder()
                .taskId(1L)
                .taskCode("S1-D01")
                .title("Configuracao Inicial do Projecto")
                .prompt("Implement the initial project setup including Spring Boot configuration...")
                .build();

        assertEquals(1L, dto.getTaskId());
        assertEquals("S1-D01", dto.getTaskCode());
        assertEquals("Configuracao Inicial do Projecto", dto.getTitle());
        assertEquals("Implement the initial project setup including Spring Boot configuration...", dto.getPrompt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        PromptDTO dto = new PromptDTO();

        assertNull(dto.getTaskId());
        assertNull(dto.getTaskCode());
        assertNull(dto.getTitle());
        assertNull(dto.getPrompt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        PromptDTO dto = new PromptDTO(
                5L, "S2-D03", "Backend Services", "Create service layer for Sprint management..."
        );

        assertEquals(5L, dto.getTaskId());
        assertEquals("S2-D03", dto.getTaskCode());
        assertEquals("Backend Services", dto.getTitle());
        assertEquals("Create service layer for Sprint management...", dto.getPrompt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        PromptDTO dto = new PromptDTO();

        dto.setTaskId(10L);
        dto.setTaskCode("S3-D15");
        dto.setTitle("Frontend Dashboard");
        dto.setPrompt("Build the Angular dashboard component with Material UI...");

        assertEquals(10L, dto.getTaskId());
        assertEquals("S3-D15", dto.getTaskCode());
        assertEquals("Frontend Dashboard", dto.getTitle());
        assertEquals("Build the Angular dashboard component with Material UI...", dto.getPrompt());
    }

    @Test
    void equals_reflexive() {
        PromptDTO dto = PromptDTO.builder().taskId(1L).taskCode("S1-D01").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        PromptDTO dto1 = PromptDTO.builder().taskId(1L).taskCode("S1-D01").title("Title").prompt("Prompt").build();
        PromptDTO dto2 = PromptDTO.builder().taskId(1L).taskCode("S1-D01").title("Title").prompt("Prompt").build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        PromptDTO dto = PromptDTO.builder().taskId(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        PromptDTO dto = PromptDTO.builder().taskId(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        PromptDTO dto1 = PromptDTO.builder().taskId(1L).taskCode("S1-D01").build();
        PromptDTO dto2 = PromptDTO.builder().taskId(2L).taskCode("S1-D02").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        PromptDTO dto1 = PromptDTO.builder().taskId(1L).taskCode("S1-D01").prompt("Prompt").build();
        PromptDTO dto2 = PromptDTO.builder().taskId(1L).taskCode("S1-D01").prompt("Prompt").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        PromptDTO dto1 = PromptDTO.builder().taskId(1L).taskCode("S1-D01").build();
        PromptDTO dto2 = PromptDTO.builder().taskId(2L).taskCode("S1-D02").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        PromptDTO dto = PromptDTO.builder()
                .taskId(1L)
                .taskCode("S1-D01")
                .title("Setup")
                .prompt("Generate code for...")
                .build();

        String result = dto.toString();
        assertTrue(result.contains("PromptDTO"));
        assertTrue(result.contains("S1-D01"));
        assertTrue(result.contains("Setup"));
        assertTrue(result.contains("Generate code for..."));
    }
}
