package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SprintRepositoryTest {

    @Autowired
    private SprintRepository sprintRepository;

    @BeforeEach
    void setUp() {
        sprintRepository.deleteAll();
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
                .startDate(LocalDate.of(2026, 3, 2).plusWeeks((long) (number - 1) * 6))
                .endDate(LocalDate.of(2026, 3, 2).plusWeeks((long) number * 6))
                .focus("Test Focus " + number)
                .color("#3884F4")
                .status(status)
                .build();
        return sprintRepository.save(sprint);
    }

    // ---- findBySprintNumber ----

    @Test
    void findBySprintNumber_shouldReturnSprintWhenExists() {
        Sprint saved = createSprint(1, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findBySprintNumber(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getSprintNumber()).isEqualTo(1);
        assertThat(result.get().getName()).isEqualTo("Sprint 1");
    }

    @Test
    void findBySprintNumber_shouldReturnEmptyWhenNotExists() {
        createSprint(1, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findBySprintNumber(99);

        assertThat(result).isEmpty();
    }

    @Test
    void findBySprintNumber_shouldReturnCorrectSprintAmongMultiple() {
        createSprint(1, SprintStatus.COMPLETED);
        Sprint sprint2 = createSprint(2, SprintStatus.ACTIVE);
        createSprint(3, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findBySprintNumber(2);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(sprint2.getId());
        assertThat(result.get().getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    // ---- findByStatus ----

    @Test
    void findByStatus_shouldReturnSprintWhenStatusMatches() {
        createSprint(1, SprintStatus.ACTIVE);

        Optional<Sprint> result = sprintRepository.findByStatus(SprintStatus.ACTIVE);

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    @Test
    void findByStatus_shouldReturnEmptyWhenNoMatch() {
        createSprint(1, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findByStatus(SprintStatus.ACTIVE);

        assertThat(result).isEmpty();
    }

    @Test
    void findFirstByStatusOrderBySprintNumberAsc_shouldReturnFirstWhenMultipleExistWithSameStatus() {
        createSprint(1, SprintStatus.PLANNED);
        createSprint(2, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED);

        assertThat(result).isPresent();
        assertThat(result.get().getSprintNumber()).isEqualTo(1);
        assertThat(result.get().getStatus()).isEqualTo(SprintStatus.PLANNED);
    }

    // ---- findAllOrdered ----

    @Test
    void findAllOrdered_shouldReturnEmptyListWhenNoSprints() {
        List<Sprint> result = sprintRepository.findAllOrdered();

        assertThat(result).isEmpty();
    }

    @Test
    void findAllOrdered_shouldReturnSprintsOrderedBySprintNumberAsc() {
        createSprint(3, SprintStatus.PLANNED);
        createSprint(1, SprintStatus.COMPLETED);
        createSprint(2, SprintStatus.ACTIVE);

        List<Sprint> result = sprintRepository.findAllOrdered();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getSprintNumber()).isEqualTo(1);
        assertThat(result.get(1).getSprintNumber()).isEqualTo(2);
        assertThat(result.get(2).getSprintNumber()).isEqualTo(3);
    }

    @Test
    void findAllOrdered_shouldReturnSingleSprint() {
        Sprint sprint = createSprint(5, SprintStatus.ACTIVE);

        List<Sprint> result = sprintRepository.findAllOrdered();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(sprint.getId());
    }

    // ---- findActiveSprint ----

    @Test
    void findActiveSprint_shouldReturnActiveSprintWhenExists() {
        createSprint(1, SprintStatus.COMPLETED);
        Sprint active = createSprint(2, SprintStatus.ACTIVE);
        createSprint(3, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findActiveSprint();

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(active.getId());
        assertThat(result.get().getSprintNumber()).isEqualTo(2);
        assertThat(result.get().getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    @Test
    void findActiveSprint_shouldReturnEmptyWhenNoActiveSprint() {
        createSprint(1, SprintStatus.COMPLETED);
        createSprint(2, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository.findActiveSprint();

        assertThat(result).isEmpty();
    }

    @Test
    void findActiveSprint_shouldReturnEmptyWhenNoSprints() {
        Optional<Sprint> result = sprintRepository.findActiveSprint();

        assertThat(result).isEmpty();
    }

    // ---- findFirstByStatusOrderBySprintNumberAsc ----

    @Test
    void findFirstByStatusOrderBySprintNumberAsc_shouldReturnFirstPlannedSprint() {
        createSprint(1, SprintStatus.COMPLETED);
        Sprint firstPlanned = createSprint(2, SprintStatus.PLANNED);
        createSprint(3, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository
                .findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(firstPlanned.getId());
        assertThat(result.get().getSprintNumber()).isEqualTo(2);
    }

    @Test
    void findFirstByStatusOrderBySprintNumberAsc_shouldReturnEmptyWhenNoMatch() {
        createSprint(1, SprintStatus.COMPLETED);
        createSprint(2, SprintStatus.COMPLETED);

        Optional<Sprint> result = sprintRepository
                .findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED);

        assertThat(result).isEmpty();
    }

    @Test
    void findFirstByStatusOrderBySprintNumberAsc_shouldReturnLowestNumberAmongMatches() {
        createSprint(5, SprintStatus.PLANNED);
        createSprint(3, SprintStatus.PLANNED);
        createSprint(4, SprintStatus.PLANNED);

        Optional<Sprint> result = sprintRepository
                .findFirstByStatusOrderBySprintNumberAsc(SprintStatus.PLANNED);

        assertThat(result).isPresent();
        assertThat(result.get().getSprintNumber()).isEqualTo(3);
    }

    @Test
    void findFirstByStatusOrderBySprintNumberAsc_shouldWorkForCompletedStatus() {
        Sprint completed1 = createSprint(1, SprintStatus.COMPLETED);
        createSprint(2, SprintStatus.ACTIVE);
        createSprint(3, SprintStatus.COMPLETED);

        Optional<Sprint> result = sprintRepository
                .findFirstByStatusOrderBySprintNumberAsc(SprintStatus.COMPLETED);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(completed1.getId());
        assertThat(result.get().getSprintNumber()).isEqualTo(1);
    }

    // ---- Basic JPA operations ----

    @Test
    void save_shouldPersistSprintAndSetId() {
        Sprint sprint = Sprint.builder()
                .sprintNumber(1)
                .name("Sprint 1 - Fundacao")
                .nameEn("Sprint 1 - Foundation")
                .weeks(6)
                .totalHours(120)
                .totalSessions(36)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 4, 12))
                .focus("Backend")
                .color("#CC092F")
                .status(SprintStatus.PLANNED)
                .build();

        Sprint saved = sprintRepository.save(sprint);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnSavedSprint() {
        Sprint saved = createSprint(1, SprintStatus.ACTIVE);

        Optional<Sprint> found = sprintRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getSprintNumber()).isEqualTo(1);
        assertThat(found.get().getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    @Test
    void delete_shouldRemoveSprint() {
        Sprint saved = createSprint(1, SprintStatus.PLANNED);

        sprintRepository.delete(saved);

        Optional<Sprint> found = sprintRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void count_shouldReturnCorrectCount() {
        createSprint(1, SprintStatus.COMPLETED);
        createSprint(2, SprintStatus.ACTIVE);
        createSprint(3, SprintStatus.PLANNED);

        long count = sprintRepository.count();

        assertThat(count).isEqualTo(3);
    }
}
