package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SprintReportTest {

    @Test
    void builder_shouldCreateSprintReportWithAllFields() {
        Sprint sprint = new Sprint();
        LocalDateTime generatedAt = LocalDateTime.of(2026, 2, 1, 18, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        SprintReport report = SprintReport.builder()
                .id(1L)
                .sprint(sprint)
                .reportType(ReportType.SPRINT_END)
                .weekNumber(4)
                .generatedAt(generatedAt)
                .summaryPt("Resumo do sprint em portugues")
                .summaryEn("Sprint summary in English")
                .metricsJson("{\"completed\": 36}")
                .pdfPath("/reports/sprint1.pdf")
                .createdAt(createdAt)
                .build();

        assertEquals(1L, report.getId());
        assertSame(sprint, report.getSprint());
        assertEquals(ReportType.SPRINT_END, report.getReportType());
        assertEquals(4, report.getWeekNumber());
        assertEquals(generatedAt, report.getGeneratedAt());
        assertEquals("Resumo do sprint em portugues", report.getSummaryPt());
        assertEquals("Sprint summary in English", report.getSummaryEn());
        assertEquals("{\"completed\": 36}", report.getMetricsJson());
        assertEquals("/reports/sprint1.pdf", report.getPdfPath());
        assertEquals(createdAt, report.getCreatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        SprintReport report = new SprintReport();
        Sprint sprint = new Sprint();
        LocalDateTime generatedAt = LocalDateTime.of(2026, 3, 15, 12, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        report.setId(2L);
        report.setSprint(sprint);
        report.setReportType(ReportType.WEEKLY);
        report.setWeekNumber(2);
        report.setGeneratedAt(generatedAt);
        report.setSummaryPt("Relatorio semanal");
        report.setSummaryEn("Weekly report");
        report.setMetricsJson("{\"tasks\": 10}");
        report.setPdfPath("/reports/week2.pdf");
        report.setCreatedAt(createdAt);

        assertEquals(2L, report.getId());
        assertSame(sprint, report.getSprint());
        assertEquals(ReportType.WEEKLY, report.getReportType());
        assertEquals(2, report.getWeekNumber());
        assertEquals(generatedAt, report.getGeneratedAt());
        assertEquals("Relatorio semanal", report.getSummaryPt());
        assertEquals("Weekly report", report.getSummaryEn());
        assertEquals("{\"tasks\": 10}", report.getMetricsJson());
        assertEquals("/reports/week2.pdf", report.getPdfPath());
        assertEquals(createdAt, report.getCreatedAt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptySprintReport() {
        SprintReport report = new SprintReport();

        assertNull(report.getId());
        assertNull(report.getSprint());
        assertNull(report.getReportType());
        assertNull(report.getWeekNumber());
        assertNull(report.getGeneratedAt());
        assertNull(report.getSummaryPt());
        assertNull(report.getSummaryEn());
        assertNull(report.getMetricsJson());
        assertNull(report.getPdfPath());
        assertNull(report.getCreatedAt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        Sprint sprint = new Sprint();
        LocalDateTime generatedAt = LocalDateTime.of(2026, 4, 1, 10, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        SprintReport report = new SprintReport(
                3L, sprint, ReportType.CUSTOM, 3,
                generatedAt, "Resumo personalizado", "Custom summary",
                "{\"custom\": true}", "/reports/custom.pdf", createdAt
        );

        assertEquals(3L, report.getId());
        assertSame(sprint, report.getSprint());
        assertEquals(ReportType.CUSTOM, report.getReportType());
        assertEquals(3, report.getWeekNumber());
        assertEquals(generatedAt, report.getGeneratedAt());
        assertEquals("Resumo personalizado", report.getSummaryPt());
        assertEquals("Custom summary", report.getSummaryEn());
        assertEquals("{\"custom\": true}", report.getMetricsJson());
        assertEquals("/reports/custom.pdf", report.getPdfPath());
        assertEquals(createdAt, report.getCreatedAt());
    }

    @Test
    void prePersist_shouldSetCreatedAt() {
        SprintReport report = new SprintReport();
        assertNull(report.getCreatedAt());

        report.onCreate();

        assertNotNull(report.getCreatedAt());
    }

    @Test
    void prePersist_createdAtShouldBeCurrentTime() {
        SprintReport report = new SprintReport();
        LocalDateTime before = LocalDateTime.now();

        report.onCreate();

        LocalDateTime after = LocalDateTime.now();
        assertNotNull(report.getCreatedAt());
        assertFalse(report.getCreatedAt().isBefore(before));
        assertFalse(report.getCreatedAt().isAfter(after));
    }
}
