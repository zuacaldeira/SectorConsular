package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.BlockType;
import ao.gov.sgcd.pm.entity.BlockedDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BlockedDayRepositoryTest {

    @Autowired
    private BlockedDayRepository blockedDayRepository;

    @BeforeEach
    void setUp() {
        blockedDayRepository.deleteAll();
    }

    // ---- Helper Methods ----

    private BlockedDay createBlockedDay(LocalDate date, BlockType type, String reason) {
        BlockedDay blockedDay = BlockedDay.builder()
                .blockedDate(date)
                .dayOfWeek(date.getDayOfWeek().name().substring(0, 3))
                .blockType(type)
                .reason(reason)
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();
        return blockedDayRepository.save(blockedDay);
    }

    // ---- findByBlockedDate ----

    @Test
    void findByBlockedDate_shouldReturnBlockedDayWhenExists() {
        LocalDate date = LocalDate.of(2026, 4, 4);
        BlockedDay saved = createBlockedDay(date, BlockType.HOLIDAY, "Dia da Paz");

        Optional<BlockedDay> result = blockedDayRepository.findByBlockedDate(date);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getReason()).isEqualTo("Dia da Paz");
        assertThat(result.get().getBlockType()).isEqualTo(BlockType.HOLIDAY);
    }

    @Test
    void findByBlockedDate_shouldReturnEmptyWhenNotExists() {
        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Dia da Paz");

        Optional<BlockedDay> result = blockedDayRepository.findByBlockedDate(
                LocalDate.of(2026, 12, 25));

        assertThat(result).isEmpty();
    }

    @Test
    void findByBlockedDate_shouldReturnCorrectDayAmongMultiple() {
        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Dia da Paz");
        BlockedDay target = createBlockedDay(LocalDate.of(2026, 6, 1),
                BlockType.HOLIDAY, "Dia Internacional da Crianca");
        createBlockedDay(LocalDate.of(2026, 9, 17), BlockType.HOLIDAY, "Heroi Nacional");

        Optional<BlockedDay> result = blockedDayRepository.findByBlockedDate(
                LocalDate.of(2026, 6, 1));

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(target.getId());
        assertThat(result.get().getReason()).isEqualTo("Dia Internacional da Crianca");
    }

    // ---- findUpcoming ----

    @Test
    void findUpcoming_shouldReturnBlockedDaysFromDateOnward() {
        LocalDate referenceDate = LocalDate.of(2026, 5, 1);

        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Past holiday");
        createBlockedDay(LocalDate.of(2026, 5, 1), BlockType.HOLIDAY, "Dia do Trabalhador");
        createBlockedDay(LocalDate.of(2026, 6, 1), BlockType.HOLIDAY, "Dia da Crianca");
        createBlockedDay(LocalDate.of(2026, 9, 17), BlockType.HOLIDAY, "Heroi Nacional");

        List<BlockedDay> result = blockedDayRepository.findUpcoming(referenceDate);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getBlockedDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(result.get(1).getBlockedDate()).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(result.get(2).getBlockedDate()).isEqualTo(LocalDate.of(2026, 9, 17));
    }

    @Test
    void findUpcoming_shouldReturnEmptyWhenNoUpcomingDays() {
        createBlockedDay(LocalDate.of(2026, 1, 1), BlockType.HOLIDAY, "Ano Novo");

        List<BlockedDay> result = blockedDayRepository.findUpcoming(LocalDate.of(2026, 12, 31));

        assertThat(result).isEmpty();
    }

    @Test
    void findUpcoming_shouldOrderByDateAsc() {
        LocalDate referenceDate = LocalDate.of(2026, 1, 1);

        createBlockedDay(LocalDate.of(2026, 9, 17), BlockType.HOLIDAY, "Heroi Nacional");
        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Dia da Paz");
        createBlockedDay(LocalDate.of(2026, 6, 1), BlockType.HOLIDAY, "Dia da Crianca");

        List<BlockedDay> result = blockedDayRepository.findUpcoming(referenceDate);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getBlockedDate()).isBefore(result.get(1).getBlockedDate());
        assertThat(result.get(1).getBlockedDate()).isBefore(result.get(2).getBlockedDate());
    }

    @Test
    void findUpcoming_shouldIncludeSccEventType() {
        createBlockedDay(LocalDate.of(2026, 5, 15), BlockType.SCC_EVENT, "Evento SCC");

        List<BlockedDay> result = blockedDayRepository.findUpcoming(LocalDate.of(2026, 5, 1));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBlockType()).isEqualTo(BlockType.SCC_EVENT);
    }

    // ---- findByDateRange ----

    @Test
    void findByDateRange_shouldReturnBlockedDaysInRange() {
        LocalDate from = LocalDate.of(2026, 4, 1);
        LocalDate to = LocalDate.of(2026, 6, 30);

        createBlockedDay(LocalDate.of(2026, 3, 15), BlockType.HOLIDAY, "Before range");
        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Dia da Paz");
        createBlockedDay(LocalDate.of(2026, 5, 1), BlockType.HOLIDAY, "Dia do Trabalhador");
        createBlockedDay(LocalDate.of(2026, 6, 1), BlockType.HOLIDAY, "Dia da Crianca");
        createBlockedDay(LocalDate.of(2026, 9, 17), BlockType.HOLIDAY, "After range");

        List<BlockedDay> result = blockedDayRepository.findByDateRange(from, to);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getReason()).isEqualTo("Dia da Paz");
        assertThat(result.get(1).getReason()).isEqualTo("Dia do Trabalhador");
        assertThat(result.get(2).getReason()).isEqualTo("Dia da Crianca");
    }

    @Test
    void findByDateRange_shouldReturnEmptyWhenNoDaysInRange() {
        createBlockedDay(LocalDate.of(2026, 1, 1), BlockType.HOLIDAY, "Ano Novo");

        List<BlockedDay> result = blockedDayRepository.findByDateRange(
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        assertThat(result).isEmpty();
    }

    @Test
    void findByDateRange_shouldIncludeBoundaryDates() {
        LocalDate from = LocalDate.of(2026, 4, 4);
        LocalDate to = LocalDate.of(2026, 6, 1);

        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Start boundary");
        createBlockedDay(LocalDate.of(2026, 6, 1), BlockType.HOLIDAY, "End boundary");

        List<BlockedDay> result = blockedDayRepository.findByDateRange(from, to);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getBlockedDate()).isEqualTo(from);
        assertThat(result.get(1).getBlockedDate()).isEqualTo(to);
    }

    @Test
    void findByDateRange_shouldOrderByDateAsc() {
        LocalDate from = LocalDate.of(2026, 1, 1);
        LocalDate to = LocalDate.of(2026, 12, 31);

        createBlockedDay(LocalDate.of(2026, 9, 17), BlockType.HOLIDAY, "Third");
        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "First");
        createBlockedDay(LocalDate.of(2026, 6, 1), BlockType.HOLIDAY, "Second");

        List<BlockedDay> result = blockedDayRepository.findByDateRange(from, to);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getBlockedDate()).isEqualTo(LocalDate.of(2026, 4, 4));
        assertThat(result.get(1).getBlockedDate()).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(result.get(2).getBlockedDate()).isEqualTo(LocalDate.of(2026, 9, 17));
    }

    // ---- Basic JPA operations ----

    @Test
    void save_shouldPersistBlockedDayAndSetId() {
        BlockedDay blockedDay = BlockedDay.builder()
                .blockedDate(LocalDate.of(2026, 4, 4))
                .dayOfWeek("SAB")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia da Paz e Reconciliacao Nacional")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        BlockedDay saved = blockedDayRepository.save(blockedDay);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBlockedDate()).isEqualTo(LocalDate.of(2026, 4, 4));
    }

    @Test
    void findById_shouldReturnSavedBlockedDay() {
        BlockedDay saved = createBlockedDay(LocalDate.of(2026, 4, 4),
                BlockType.HOLIDAY, "Dia da Paz");

        Optional<BlockedDay> found = blockedDayRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getReason()).isEqualTo("Dia da Paz");
        assertThat(found.get().getHoursLost()).isEqualByComparingTo(BigDecimal.valueOf(3.5));
    }

    @Test
    void delete_shouldRemoveBlockedDay() {
        BlockedDay saved = createBlockedDay(LocalDate.of(2026, 4, 4),
                BlockType.HOLIDAY, "Dia da Paz");

        blockedDayRepository.delete(saved);

        Optional<BlockedDay> found = blockedDayRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void count_shouldReturnCorrectCount() {
        createBlockedDay(LocalDate.of(2026, 4, 4), BlockType.HOLIDAY, "Dia da Paz");
        createBlockedDay(LocalDate.of(2026, 5, 1), BlockType.HOLIDAY, "Dia do Trabalhador");
        createBlockedDay(LocalDate.of(2026, 5, 15), BlockType.SCC_EVENT, "Evento SCC");

        long count = blockedDayRepository.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    void save_shouldUpdateExistingBlockedDay() {
        BlockedDay saved = createBlockedDay(LocalDate.of(2026, 4, 4),
                BlockType.HOLIDAY, "Original reason");

        saved.setReason("Updated reason");
        saved.setHoursLost(BigDecimal.valueOf(7.0));
        blockedDayRepository.save(saved);

        Optional<BlockedDay> found = blockedDayRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getReason()).isEqualTo("Updated reason");
        assertThat(found.get().getHoursLost()).isEqualByComparingTo(BigDecimal.valueOf(7.0));
    }
}
