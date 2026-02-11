package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.ReportDTO;
import ao.gov.sgcd.pm.entity.ReportType;
import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintReport;
import ao.gov.sgcd.pm.entity.SprintStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportMapperTest {

    private final ReportMapper mapper = Mappers.getMapper(ReportMapper.class);

    @Test
    void toDto_shouldMapAllFieldsIncludingSprintNestedFields() {
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
                .status(SprintStatus.COMPLETED)
                .build();

        LocalDateTime generatedAt = LocalDateTime.of(2026, 2, 1, 18, 0, 0);
        LocalDateTime createdAt = LocalDateTime.of(2026, 2, 1, 18, 0, 5);

        SprintReport report = SprintReport.builder()
                .id(1L)
                .sprint(sprint)
                .reportType(ReportType.SPRINT_END)
                .weekNumber(4)
                .generatedAt(generatedAt)
                .summaryPt("Sprint 1 concluido com sucesso. Todas as tarefas de fundacao completadas.")
                .summaryEn("Sprint 1 completed successfully. All foundation tasks completed.")
                .metricsJson("{\"tasksCompleted\": 36, \"hoursSpent\": 118.5}")
                .pdfPath("/reports/sprint-1-end-report.pdf")
                .createdAt(createdAt)
                .build();

        ReportDTO dto = mapper.toDto(report);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getSprintId());
        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", dto.getSprintName());
        assertEquals(ReportType.SPRINT_END, dto.getReportType());
        assertEquals(4, dto.getWeekNumber());
        assertEquals(generatedAt, dto.getGeneratedAt());
        assertEquals("Sprint 1 concluido com sucesso. Todas as tarefas de fundacao completadas.", dto.getSummaryPt());
        assertEquals("Sprint 1 completed successfully. All foundation tasks completed.", dto.getSummaryEn());
        assertEquals("{\"tasksCompleted\": 36, \"hoursSpent\": 118.5}", dto.getMetricsJson());
        assertEquals("/reports/sprint-1-end-report.pdf", dto.getPdfPath());
    }

    @Test
    void toDto_shouldMapSprintIdFromNestedSprint() {
        Sprint sprint = Sprint.builder()
                .id(3L)
                .sprintNumber(3)
                .name("Sprint 3")
                .startDate(LocalDate.of(2026, 3, 1))
                .endDate(LocalDate.of(2026, 4, 1))
                .build();

        SprintReport report = SprintReport.builder()
                .id(10L)
                .sprint(sprint)
                .reportType(ReportType.WEEKLY)
                .generatedAt(LocalDateTime.now())
                .build();

        ReportDTO dto = mapper.toDto(report);

        assertNotNull(dto);
        assertEquals(3L, dto.getSprintId());
        assertEquals(3, dto.getSprintNumber());
        assertEquals("Sprint 3", dto.getSprintName());
    }

    @Test
    void toDto_shouldHandleNullSprintGracefully() {
        SprintReport report = SprintReport.builder()
                .id(1L)
                .sprint(null)
                .reportType(ReportType.CUSTOM)
                .generatedAt(LocalDateTime.of(2026, 2, 10, 12, 0, 0))
                .summaryPt("Relatorio personalizado")
                .build();

        ReportDTO dto = mapper.toDto(report);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getSprintId());
        assertNull(dto.getSprintNumber());
        assertNull(dto.getSprintName());
        assertEquals(ReportType.CUSTOM, dto.getReportType());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        ReportDTO dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toDto_shouldMapAllReportTypes() {
        Sprint sprint = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1")
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .build();

        for (ReportType reportType : ReportType.values()) {
            SprintReport report = SprintReport.builder()
                    .id(1L)
                    .sprint(sprint)
                    .reportType(reportType)
                    .generatedAt(LocalDateTime.now())
                    .build();

            ReportDTO dto = mapper.toDto(report);

            assertNotNull(dto);
            assertEquals(reportType, dto.getReportType());
        }
    }

    @Test
    void toDto_shouldHandleNullOptionalFields() {
        Sprint sprint = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1")
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .build();

        SprintReport report = SprintReport.builder()
                .id(1L)
                .sprint(sprint)
                .reportType(ReportType.WEEKLY)
                .generatedAt(LocalDateTime.of(2026, 1, 12, 18, 0, 0))
                .build();
        // weekNumber, summaryPt, summaryEn, metricsJson, pdfPath, createdAt are null

        ReportDTO dto = mapper.toDto(report);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getSprintId());
        assertNull(dto.getWeekNumber());
        assertNull(dto.getSummaryPt());
        assertNull(dto.getSummaryEn());
        assertNull(dto.getMetricsJson());
        assertNull(dto.getPdfPath());
    }

    @Test
    void toDtoList_shouldMapAllElementsInList() {
        Sprint sprint1 = Sprint.builder()
                .id(1L)
                .sprintNumber(1)
                .name("Sprint 1 - Fundacao")
                .startDate(LocalDate.of(2026, 1, 5))
                .endDate(LocalDate.of(2026, 2, 1))
                .build();

        Sprint sprint2 = Sprint.builder()
                .id(2L)
                .sprintNumber(2)
                .name("Sprint 2 - Core")
                .startDate(LocalDate.of(2026, 2, 3))
                .endDate(LocalDate.of(2026, 2, 22))
                .build();

        SprintReport report1 = SprintReport.builder()
                .id(1L)
                .sprint(sprint1)
                .reportType(ReportType.SPRINT_END)
                .weekNumber(4)
                .generatedAt(LocalDateTime.of(2026, 2, 1, 18, 0, 0))
                .summaryPt("Sprint 1 concluido")
                .build();

        SprintReport report2 = SprintReport.builder()
                .id(2L)
                .sprint(sprint2)
                .reportType(ReportType.WEEKLY)
                .weekNumber(1)
                .generatedAt(LocalDateTime.of(2026, 2, 8, 18, 0, 0))
                .summaryPt("Semana 1 do Sprint 2")
                .build();

        List<ReportDTO> dtos = mapper.toDtoList(Arrays.asList(report1, report2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(1L, dtos.get(0).getSprintId());
        assertEquals("Sprint 1 - Fundacao", dtos.get(0).getSprintName());
        assertEquals(ReportType.SPRINT_END, dtos.get(0).getReportType());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(2L, dtos.get(1).getSprintId());
        assertEquals("Sprint 2 - Core", dtos.get(1).getSprintName());
        assertEquals(ReportType.WEEKLY, dtos.get(1).getReportType());
    }

    @Test
    void toDtoList_shouldReturnEmptyListForEmptyInput() {
        List<ReportDTO> dtos = mapper.toDtoList(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullForNullInput() {
        List<ReportDTO> dtos = mapper.toDtoList(null);

        assertNull(dtos);
    }

    @Test
    void toDto_shouldMapWeeklyReportWithWeekNumber() {
        Sprint sprint = Sprint.builder()
                .id(2L)
                .sprintNumber(2)
                .name("Sprint 2 - Core")
                .startDate(LocalDate.of(2026, 2, 3))
                .endDate(LocalDate.of(2026, 2, 22))
                .build();

        SprintReport report = SprintReport.builder()
                .id(5L)
                .sprint(sprint)
                .reportType(ReportType.WEEKLY)
                .weekNumber(2)
                .generatedAt(LocalDateTime.of(2026, 2, 15, 18, 0, 0))
                .summaryPt("Semana 2 - Progresso significativo")
                .summaryEn("Week 2 - Significant progress")
                .metricsJson("{\"tasksCompleted\": 10, \"hoursSpent\": 35}")
                .build();

        ReportDTO dto = mapper.toDto(report);

        assertNotNull(dto);
        assertEquals(2, dto.getWeekNumber());
        assertEquals(ReportType.WEEKLY, dto.getReportType());
        assertEquals(2, dto.getSprintNumber());
    }
}
