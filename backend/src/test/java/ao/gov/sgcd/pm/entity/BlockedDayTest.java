package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BlockedDayTest {

    @Test
    void builder_shouldCreateBlockedDayWithAllFields() {
        LocalDate blockedDate = LocalDate.of(2026, 2, 4);

        BlockedDay blockedDay = BlockedDay.builder()
                .id(1L)
                .blockedDate(blockedDate)
                .dayOfWeek("QUA")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia dos Herois Nacionais")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        assertEquals(1L, blockedDay.getId());
        assertEquals(blockedDate, blockedDay.getBlockedDate());
        assertEquals("QUA", blockedDay.getDayOfWeek());
        assertEquals(BlockType.HOLIDAY, blockedDay.getBlockType());
        assertEquals("Dia dos Herois Nacionais", blockedDay.getReason());
        assertEquals(BigDecimal.valueOf(3.5), blockedDay.getHoursLost());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        BlockedDay blockedDay = new BlockedDay();
        LocalDate blockedDate = LocalDate.of(2026, 3, 8);

        blockedDay.setId(2L);
        blockedDay.setBlockedDate(blockedDate);
        blockedDay.setDayOfWeek("SEX");
        blockedDay.setBlockType(BlockType.SCC_EVENT);
        blockedDay.setReason("Evento consular");
        blockedDay.setHoursLost(BigDecimal.valueOf(4.0));

        assertEquals(2L, blockedDay.getId());
        assertEquals(blockedDate, blockedDay.getBlockedDate());
        assertEquals("SEX", blockedDay.getDayOfWeek());
        assertEquals(BlockType.SCC_EVENT, blockedDay.getBlockType());
        assertEquals("Evento consular", blockedDay.getReason());
        assertEquals(BigDecimal.valueOf(4.0), blockedDay.getHoursLost());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyBlockedDay() {
        BlockedDay blockedDay = new BlockedDay();

        assertNull(blockedDay.getId());
        assertNull(blockedDay.getBlockedDate());
        assertNull(blockedDay.getDayOfWeek());
        assertNull(blockedDay.getBlockType());
        assertNull(blockedDay.getReason());
        assertNull(blockedDay.getHoursLost());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDate blockedDate = LocalDate.of(2026, 11, 11);

        BlockedDay blockedDay = new BlockedDay(
                3L, blockedDate, "QUA", BlockType.HOLIDAY,
                "Dia da Independencia", BigDecimal.valueOf(3.5)
        );

        assertEquals(3L, blockedDay.getId());
        assertEquals(blockedDate, blockedDay.getBlockedDate());
        assertEquals("QUA", blockedDay.getDayOfWeek());
        assertEquals(BlockType.HOLIDAY, blockedDay.getBlockType());
        assertEquals("Dia da Independencia", blockedDay.getReason());
        assertEquals(BigDecimal.valueOf(3.5), blockedDay.getHoursLost());
    }

    @Test
    void entity_shouldHaveNoLifecycleCallbacks() {
        // BlockedDay has no @PrePersist or @PreUpdate callbacks
        // This test verifies that fields remain null after construction
        BlockedDay blockedDay = new BlockedDay();

        assertNull(blockedDay.getId());
        assertNull(blockedDay.getBlockedDate());
        assertNull(blockedDay.getDayOfWeek());
        assertNull(blockedDay.getBlockType());
        assertNull(blockedDay.getReason());
        assertNull(blockedDay.getHoursLost());
    }

    @Test
    void builder_shouldHandleZeroHoursLost() {
        BlockedDay blockedDay = BlockedDay.builder()
                .hoursLost(BigDecimal.ZERO)
                .build();

        assertEquals(BigDecimal.ZERO, blockedDay.getHoursLost());
    }

    @Test
    void setter_shouldAllowChangingBlockType() {
        BlockedDay blockedDay = new BlockedDay();
        blockedDay.setBlockType(BlockType.HOLIDAY);
        assertEquals(BlockType.HOLIDAY, blockedDay.getBlockType());

        blockedDay.setBlockType(BlockType.SCC_EVENT);
        assertEquals(BlockType.SCC_EVENT, blockedDay.getBlockType());
    }
}
