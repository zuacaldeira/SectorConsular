package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.ReportType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        LocalDateTime generatedAt = LocalDateTime.of(2026, 2, 1, 18, 0);

        ReportDTO dto = ReportDTO.builder()
                .id(1L)
                .sprintId(1L)
                .sprintNumber(1)
                .sprintName("Sprint 1 - Fundacao")
                .reportType(ReportType.SPRINT_END)
                .weekNumber(4)
                .generatedAt(generatedAt)
                .summaryPt("Resumo do sprint em portugues")
                .summaryEn("Sprint summary in English")
                .metricsJson("{\"completedTasks\": 36}")
                .pdfPath("/reports/sprint1-end.pdf")
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getSprintId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", dto.getSprintName());
        assertEquals(ReportType.SPRINT_END, dto.getReportType());
        assertEquals(4, dto.getWeekNumber());
        assertEquals(generatedAt, dto.getGeneratedAt());
        assertEquals("Resumo do sprint em portugues", dto.getSummaryPt());
        assertEquals("Sprint summary in English", dto.getSummaryEn());
        assertEquals("{\"completedTasks\": 36}", dto.getMetricsJson());
        assertEquals("/reports/sprint1-end.pdf", dto.getPdfPath());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        ReportDTO dto = new ReportDTO();

        assertNull(dto.getId());
        assertNull(dto.getSprintId());
        assertNull(dto.getSprintNumber());
        assertNull(dto.getSprintName());
        assertNull(dto.getReportType());
        assertNull(dto.getWeekNumber());
        assertNull(dto.getGeneratedAt());
        assertNull(dto.getSummaryPt());
        assertNull(dto.getSummaryEn());
        assertNull(dto.getMetricsJson());
        assertNull(dto.getPdfPath());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDateTime generatedAt = LocalDateTime.of(2026, 1, 10, 17, 0);

        ReportDTO dto = new ReportDTO(
                2L, 1L, 1, "Sprint 1", ReportType.WEEKLY, 1,
                generatedAt, "Resumo semanal", "Weekly summary",
                "{\"tasks\": 5}", "/reports/week1.pdf"
        );

        assertEquals(2L, dto.getId());
        assertEquals(1L, dto.getSprintId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1", dto.getSprintName());
        assertEquals(ReportType.WEEKLY, dto.getReportType());
        assertEquals(1, dto.getWeekNumber());
        assertEquals(generatedAt, dto.getGeneratedAt());
        assertEquals("Resumo semanal", dto.getSummaryPt());
        assertEquals("Weekly summary", dto.getSummaryEn());
        assertEquals("{\"tasks\": 5}", dto.getMetricsJson());
        assertEquals("/reports/week1.pdf", dto.getPdfPath());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        ReportDTO dto = new ReportDTO();
        LocalDateTime generatedAt = LocalDateTime.of(2026, 3, 15, 12, 0);

        dto.setId(3L);
        dto.setSprintId(2L);
        dto.setSprintNumber(2);
        dto.setSprintName("Sprint 2");
        dto.setReportType(ReportType.CUSTOM);
        dto.setWeekNumber(2);
        dto.setGeneratedAt(generatedAt);
        dto.setSummaryPt("Resumo personalizado");
        dto.setSummaryEn("Custom summary");
        dto.setMetricsJson("{\"custom\": true}");
        dto.setPdfPath("/reports/custom.pdf");

        assertEquals(3L, dto.getId());
        assertEquals(2L, dto.getSprintId());
        assertEquals(2, dto.getSprintNumber());
        assertEquals("Sprint 2", dto.getSprintName());
        assertEquals(ReportType.CUSTOM, dto.getReportType());
        assertEquals(2, dto.getWeekNumber());
        assertEquals(generatedAt, dto.getGeneratedAt());
        assertEquals("Resumo personalizado", dto.getSummaryPt());
        assertEquals("Custom summary", dto.getSummaryEn());
        assertEquals("{\"custom\": true}", dto.getMetricsJson());
        assertEquals("/reports/custom.pdf", dto.getPdfPath());
    }

    @Test
    void gettersAndSetters_shouldWorkWithAllReportTypes() {
        ReportDTO dto = new ReportDTO();

        dto.setReportType(ReportType.WEEKLY);
        assertEquals(ReportType.WEEKLY, dto.getReportType());

        dto.setReportType(ReportType.SPRINT_END);
        assertEquals(ReportType.SPRINT_END, dto.getReportType());

        dto.setReportType(ReportType.CUSTOM);
        assertEquals(ReportType.CUSTOM, dto.getReportType());
    }

    @Test
    void equals_reflexive() {
        ReportDTO dto = ReportDTO.builder().id(1L).sprintName("Sprint 1").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        LocalDateTime generatedAt = LocalDateTime.of(2026, 1, 10, 17, 0);
        ReportDTO dto1 = ReportDTO.builder().id(1L).sprintId(1L).reportType(ReportType.WEEKLY).generatedAt(generatedAt).build();
        ReportDTO dto2 = ReportDTO.builder().id(1L).sprintId(1L).reportType(ReportType.WEEKLY).generatedAt(generatedAt).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        ReportDTO dto = ReportDTO.builder().id(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        ReportDTO dto = ReportDTO.builder().id(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        ReportDTO dto1 = ReportDTO.builder().id(1L).reportType(ReportType.WEEKLY).build();
        ReportDTO dto2 = ReportDTO.builder().id(2L).reportType(ReportType.SPRINT_END).build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        ReportDTO dto1 = ReportDTO.builder().id(1L).sprintId(1L).reportType(ReportType.WEEKLY).build();
        ReportDTO dto2 = ReportDTO.builder().id(1L).sprintId(1L).reportType(ReportType.WEEKLY).build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        ReportDTO dto1 = ReportDTO.builder().id(1L).reportType(ReportType.WEEKLY).build();
        ReportDTO dto2 = ReportDTO.builder().id(2L).reportType(ReportType.SPRINT_END).build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        ReportDTO dto = ReportDTO.builder()
                .id(1L)
                .sprintName("Sprint 1")
                .reportType(ReportType.SPRINT_END)
                .summaryPt("Resumo")
                .build();

        String result = dto.toString();
        assertTrue(result.contains("ReportDTO"));
        assertTrue(result.contains("Sprint 1"));
        assertTrue(result.contains("SPRINT_END"));
        assertTrue(result.contains("Resumo"));
    }
}
