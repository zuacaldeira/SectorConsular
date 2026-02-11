package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SprintReportRepositoryTest {

    @Autowired
    private SprintReportRepository sprintReportRepository;

    @Autowired
    private SprintRepository sprintRepository;

    private Sprint defaultSprint;

    @BeforeEach
    void setUp() {
        sprintReportRepository.deleteAll();
        sprintRepository.deleteAll();
        defaultSprint = createSprint(1, SprintStatus.ACTIVE);
    }

    // ---- Helper Methods ----

    private Sprint createSprint(int number, SprintStatus status) {
        Sprint sprint = Sprint.builder()
                .sprintNumber(number)
                .name("Sprint " + number)
                .nameEn("Sprint " + number + " EN")
                .weeks(6)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 5, 10))
                .focus("Test")
                .color("#3884F4")
                .status(status)
                .build();
        return sprintRepository.save(sprint);
    }

    private SprintReport createReport(Sprint sprint, ReportType type,
                                       Integer weekNumber, LocalDateTime generatedAt) {
        SprintReport report = SprintReport.builder()
                .sprint(sprint)
                .reportType(type)
                .weekNumber(weekNumber)
                .generatedAt(generatedAt)
                .summaryPt("Resumo do relatorio em portugues")
                .summaryEn("Report summary in English")
                .metricsJson("{\"completed\": 10, \"total\": 36}")
                .pdfPath("/reports/sprint-" + sprint.getSprintNumber() + "-report.pdf")
                .build();
        return sprintReportRepository.save(report);
    }

    // ---- findBySprintIdOrderByGeneratedAtDesc ----

    @Test
    void findBySprintIdOrderByGeneratedAtDesc_shouldReturnReportsForSprint() {
        LocalDateTime time1 = LocalDateTime.of(2026, 3, 9, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2026, 3, 16, 10, 0);

        createReport(defaultSprint, ReportType.WEEKLY, 1, time1);
        createReport(defaultSprint, ReportType.WEEKLY, 2, time2);

        List<SprintReport> result = sprintReportRepository
                .findBySprintIdOrderByGeneratedAtDesc(defaultSprint.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void findBySprintIdOrderByGeneratedAtDesc_shouldOrderByGeneratedAtDescending() {
        LocalDateTime early = LocalDateTime.of(2026, 3, 9, 10, 0);
        LocalDateTime mid = LocalDateTime.of(2026, 3, 16, 10, 0);
        LocalDateTime late = LocalDateTime.of(2026, 3, 23, 10, 0);

        createReport(defaultSprint, ReportType.WEEKLY, 1, early);
        createReport(defaultSprint, ReportType.WEEKLY, 3, late);
        createReport(defaultSprint, ReportType.WEEKLY, 2, mid);

        List<SprintReport> result = sprintReportRepository
                .findBySprintIdOrderByGeneratedAtDesc(defaultSprint.getId());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getGeneratedAt()).isEqualTo(late);
        assertThat(result.get(1).getGeneratedAt()).isEqualTo(mid);
        assertThat(result.get(2).getGeneratedAt()).isEqualTo(early);
    }

    @Test
    void findBySprintIdOrderByGeneratedAtDesc_shouldReturnEmptyForSprintWithNoReports() {
        List<SprintReport> result = sprintReportRepository
                .findBySprintIdOrderByGeneratedAtDesc(defaultSprint.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findBySprintIdOrderByGeneratedAtDesc_shouldReturnEmptyForNonexistentSprint() {
        List<SprintReport> result = sprintReportRepository
                .findBySprintIdOrderByGeneratedAtDesc(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findBySprintIdOrderByGeneratedAtDesc_shouldNotReturnReportsFromOtherSprints() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);

        createReport(defaultSprint, ReportType.WEEKLY, 1,
                LocalDateTime.of(2026, 3, 9, 10, 0));
        createReport(sprint2, ReportType.WEEKLY, 1,
                LocalDateTime.of(2026, 5, 18, 10, 0));

        List<SprintReport> result = sprintReportRepository
                .findBySprintIdOrderByGeneratedAtDesc(defaultSprint.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSprint().getId()).isEqualTo(defaultSprint.getId());
    }

    // ---- findAllByOrderByGeneratedAtDesc ----

    @Test
    void findAllByOrderByGeneratedAtDesc_shouldReturnAllReportsOrderedByGeneratedAtDesc() {
        Sprint sprint2 = createSprint(2, SprintStatus.PLANNED);

        LocalDateTime time1 = LocalDateTime.of(2026, 3, 9, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2026, 3, 16, 10, 0);
        LocalDateTime time3 = LocalDateTime.of(2026, 5, 18, 10, 0);

        createReport(defaultSprint, ReportType.WEEKLY, 1, time1);
        createReport(sprint2, ReportType.WEEKLY, 1, time3);
        createReport(defaultSprint, ReportType.WEEKLY, 2, time2);

        List<SprintReport> result = sprintReportRepository.findAllByOrderByGeneratedAtDesc();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getGeneratedAt()).isEqualTo(time3);
        assertThat(result.get(1).getGeneratedAt()).isEqualTo(time2);
        assertThat(result.get(2).getGeneratedAt()).isEqualTo(time1);
    }

    @Test
    void findAllByOrderByGeneratedAtDesc_shouldReturnEmptyWhenNoReports() {
        List<SprintReport> result = sprintReportRepository.findAllByOrderByGeneratedAtDesc();

        assertThat(result).isEmpty();
    }

    @Test
    void findAllByOrderByGeneratedAtDesc_shouldIncludeAllReportTypes() {
        LocalDateTime time1 = LocalDateTime.of(2026, 3, 9, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2026, 4, 12, 10, 0);
        LocalDateTime time3 = LocalDateTime.of(2026, 3, 20, 10, 0);

        createReport(defaultSprint, ReportType.WEEKLY, 1, time1);
        createReport(defaultSprint, ReportType.SPRINT_END, null, time2);
        createReport(defaultSprint, ReportType.CUSTOM, null, time3);

        List<SprintReport> result = sprintReportRepository.findAllByOrderByGeneratedAtDesc();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getReportType()).isEqualTo(ReportType.SPRINT_END);
        assertThat(result.get(1).getReportType()).isEqualTo(ReportType.CUSTOM);
        assertThat(result.get(2).getReportType()).isEqualTo(ReportType.WEEKLY);
    }

    // ---- Basic JPA operations ----

    @Test
    void save_shouldPersistReportAndSetId() {
        SprintReport report = SprintReport.builder()
                .sprint(defaultSprint)
                .reportType(ReportType.WEEKLY)
                .weekNumber(1)
                .generatedAt(LocalDateTime.of(2026, 3, 9, 10, 0))
                .summaryPt("Resumo semanal")
                .summaryEn("Weekly summary")
                .metricsJson("{\"tasks_completed\": 6}")
                .build();

        SprintReport saved = sprintReportRepository.save(report);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnSavedReport() {
        SprintReport saved = createReport(defaultSprint, ReportType.SPRINT_END,
                null, LocalDateTime.of(2026, 4, 12, 10, 0));

        Optional<SprintReport> found = sprintReportRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getReportType()).isEqualTo(ReportType.SPRINT_END);
        assertThat(found.get().getSummaryPt()).isEqualTo("Resumo do relatorio em portugues");
        assertThat(found.get().getSummaryEn()).isEqualTo("Report summary in English");
    }

    @Test
    void delete_shouldRemoveReport() {
        SprintReport saved = createReport(defaultSprint, ReportType.WEEKLY,
                1, LocalDateTime.of(2026, 3, 9, 10, 0));

        sprintReportRepository.delete(saved);

        Optional<SprintReport> found = sprintReportRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void count_shouldReturnCorrectCount() {
        createReport(defaultSprint, ReportType.WEEKLY, 1,
                LocalDateTime.of(2026, 3, 9, 10, 0));
        createReport(defaultSprint, ReportType.WEEKLY, 2,
                LocalDateTime.of(2026, 3, 16, 10, 0));
        createReport(defaultSprint, ReportType.SPRINT_END, null,
                LocalDateTime.of(2026, 4, 12, 10, 0));

        long count = sprintReportRepository.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    void save_shouldAllowNullWeekNumber() {
        SprintReport report = SprintReport.builder()
                .sprint(defaultSprint)
                .reportType(ReportType.SPRINT_END)
                .generatedAt(LocalDateTime.of(2026, 4, 12, 10, 0))
                .summaryPt("Relatorio final do sprint")
                .build();

        SprintReport saved = sprintReportRepository.save(report);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getWeekNumber()).isNull();
    }

    @Test
    void save_shouldUpdateExistingReport() {
        SprintReport saved = createReport(defaultSprint, ReportType.WEEKLY,
                1, LocalDateTime.of(2026, 3, 9, 10, 0));

        saved.setSummaryPt("Resumo atualizado");
        saved.setPdfPath("/reports/updated-report.pdf");
        sprintReportRepository.save(saved);

        Optional<SprintReport> found = sprintReportRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSummaryPt()).isEqualTo("Resumo atualizado");
        assertThat(found.get().getPdfPath()).isEqualTo("/reports/updated-report.pdf");
    }
}
