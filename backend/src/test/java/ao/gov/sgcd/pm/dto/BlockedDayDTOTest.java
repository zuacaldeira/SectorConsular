package ao.gov.sgcd.pm.dto;

import ao.gov.sgcd.pm.entity.BlockType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BlockedDayDTOTest {

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        LocalDate blockedDate = LocalDate.of(2026, 2, 4);

        BlockedDayDTO dto = BlockedDayDTO.builder()
                .id(1L)
                .blockedDate(blockedDate)
                .dayOfWeek("WEDNESDAY")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia dos Herois Nacionais")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(blockedDate, dto.getBlockedDate());
        assertEquals("WEDNESDAY", dto.getDayOfWeek());
        assertEquals(BlockType.HOLIDAY, dto.getBlockType());
        assertEquals("Dia dos Herois Nacionais", dto.getReason());
        assertEquals(BigDecimal.valueOf(3.5), dto.getHoursLost());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        BlockedDayDTO dto = new BlockedDayDTO();

        assertNull(dto.getId());
        assertNull(dto.getBlockedDate());
        assertNull(dto.getDayOfWeek());
        assertNull(dto.getBlockType());
        assertNull(dto.getReason());
        assertNull(dto.getHoursLost());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDate blockedDate = LocalDate.of(2026, 3, 8);

        BlockedDayDTO dto = new BlockedDayDTO(
                2L, blockedDate, "SUNDAY", BlockType.SCC_EVENT,
                "Evento SCC", BigDecimal.valueOf(4.0)
        );

        assertEquals(2L, dto.getId());
        assertEquals(blockedDate, dto.getBlockedDate());
        assertEquals("SUNDAY", dto.getDayOfWeek());
        assertEquals(BlockType.SCC_EVENT, dto.getBlockType());
        assertEquals("Evento SCC", dto.getReason());
        assertEquals(BigDecimal.valueOf(4.0), dto.getHoursLost());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        BlockedDayDTO dto = new BlockedDayDTO();
        LocalDate blockedDate = LocalDate.of(2026, 4, 4);

        dto.setId(3L);
        dto.setBlockedDate(blockedDate);
        dto.setDayOfWeek("FRIDAY");
        dto.setBlockType(BlockType.HOLIDAY);
        dto.setReason("Dia da Paz");
        dto.setHoursLost(BigDecimal.valueOf(3.0));

        assertEquals(3L, dto.getId());
        assertEquals(blockedDate, dto.getBlockedDate());
        assertEquals("FRIDAY", dto.getDayOfWeek());
        assertEquals(BlockType.HOLIDAY, dto.getBlockType());
        assertEquals("Dia da Paz", dto.getReason());
        assertEquals(BigDecimal.valueOf(3.0), dto.getHoursLost());
    }

    @Test
    void gettersAndSetters_shouldWorkWithAllBlockTypes() {
        BlockedDayDTO dto = new BlockedDayDTO();

        dto.setBlockType(BlockType.HOLIDAY);
        assertEquals(BlockType.HOLIDAY, dto.getBlockType());

        dto.setBlockType(BlockType.SCC_EVENT);
        assertEquals(BlockType.SCC_EVENT, dto.getBlockType());
    }

    @Test
    void equals_reflexive() {
        BlockedDayDTO dto = BlockedDayDTO.builder().id(1L).reason("Feriado").build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        LocalDate date = LocalDate.of(2026, 2, 4);
        BlockedDayDTO dto1 = BlockedDayDTO.builder().id(1L).blockedDate(date).blockType(BlockType.HOLIDAY).build();
        BlockedDayDTO dto2 = BlockedDayDTO.builder().id(1L).blockedDate(date).blockType(BlockType.HOLIDAY).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        BlockedDayDTO dto = BlockedDayDTO.builder().id(1L).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        BlockedDayDTO dto = BlockedDayDTO.builder().id(1L).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        BlockedDayDTO dto1 = BlockedDayDTO.builder().id(1L).reason("Holiday").build();
        BlockedDayDTO dto2 = BlockedDayDTO.builder().id(2L).reason("Event").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        LocalDate date = LocalDate.of(2026, 2, 4);
        BlockedDayDTO dto1 = BlockedDayDTO.builder().id(1L).blockedDate(date).reason("Holiday").build();
        BlockedDayDTO dto2 = BlockedDayDTO.builder().id(1L).blockedDate(date).reason("Holiday").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        BlockedDayDTO dto1 = BlockedDayDTO.builder().id(1L).reason("Holiday").build();
        BlockedDayDTO dto2 = BlockedDayDTO.builder().id(2L).reason("Event").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        BlockedDayDTO dto = BlockedDayDTO.builder()
                .id(1L)
                .reason("Dia dos Herois")
                .blockType(BlockType.HOLIDAY)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("BlockedDayDTO"));
        assertTrue(result.contains("Dia dos Herois"));
        assertTrue(result.contains("HOLIDAY"));
    }
}
