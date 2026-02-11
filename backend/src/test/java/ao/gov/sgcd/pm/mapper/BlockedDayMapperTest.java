package ao.gov.sgcd.pm.mapper;

import ao.gov.sgcd.pm.dto.BlockedDayDTO;
import ao.gov.sgcd.pm.entity.BlockType;
import ao.gov.sgcd.pm.entity.BlockedDay;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockedDayMapperTest {

    private final BlockedDayMapper mapper = Mappers.getMapper(BlockedDayMapper.class);

    @Test
    void toDto_shouldMapAllFieldsDirectly() {
        BlockedDay blockedDay = BlockedDay.builder()
                .id(1L)
                .blockedDate(LocalDate.of(2026, 2, 4))
                .dayOfWeek("WED")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia dos Heróis Nacionais")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        BlockedDayDTO dto = mapper.toDto(blockedDay);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(LocalDate.of(2026, 2, 4), dto.getBlockedDate());
        assertEquals("WED", dto.getDayOfWeek());
        assertEquals(BlockType.HOLIDAY, dto.getBlockType());
        assertEquals("Dia dos Heróis Nacionais", dto.getReason());
        assertEquals(BigDecimal.valueOf(3.5), dto.getHoursLost());
    }

    @Test
    void toDto_shouldMapHolidayBlockType() {
        BlockedDay blockedDay = BlockedDay.builder()
                .id(1L)
                .blockedDate(LocalDate.of(2026, 3, 8))
                .dayOfWeek("SUN")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia Internacional da Mulher")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        BlockedDayDTO dto = mapper.toDto(blockedDay);

        assertNotNull(dto);
        assertEquals(BlockType.HOLIDAY, dto.getBlockType());
    }

    @Test
    void toDto_shouldMapSccEventBlockType() {
        BlockedDay blockedDay = BlockedDay.builder()
                .id(2L)
                .blockedDate(LocalDate.of(2026, 4, 15))
                .dayOfWeek("WED")
                .blockType(BlockType.SCC_EVENT)
                .reason("Evento do Sector Consular")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        BlockedDayDTO dto = mapper.toDto(blockedDay);

        assertNotNull(dto);
        assertEquals(BlockType.SCC_EVENT, dto.getBlockType());
    }

    @Test
    void toDto_shouldReturnNullForNullInput() {
        BlockedDayDTO dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toDtoList_shouldMapAllElementsInList() {
        BlockedDay day1 = BlockedDay.builder()
                .id(1L)
                .blockedDate(LocalDate.of(2026, 2, 4))
                .dayOfWeek("WED")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia dos Heróis Nacionais")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        BlockedDay day2 = BlockedDay.builder()
                .id(2L)
                .blockedDate(LocalDate.of(2026, 3, 4))
                .dayOfWeek("WED")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia do Carnaval")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        BlockedDay day3 = BlockedDay.builder()
                .id(3L)
                .blockedDate(LocalDate.of(2026, 5, 1))
                .dayOfWeek("FRI")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia do Trabalhador")
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();

        List<BlockedDayDTO> dtos = mapper.toDtoList(Arrays.asList(day1, day2, day3));

        assertNotNull(dtos);
        assertEquals(3, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("Dia dos Heróis Nacionais", dtos.get(0).getReason());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Dia do Carnaval", dtos.get(1).getReason());
        assertEquals(3L, dtos.get(2).getId());
        assertEquals("Dia do Trabalhador", dtos.get(2).getReason());
    }

    @Test
    void toDtoList_shouldReturnEmptyListForEmptyInput() {
        List<BlockedDayDTO> dtos = mapper.toDtoList(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnNullForNullInput() {
        List<BlockedDayDTO> dtos = mapper.toDtoList(null);

        assertNull(dtos);
    }

    @Test
    void toDto_shouldMapAllBlockTypes() {
        for (BlockType blockType : BlockType.values()) {
            BlockedDay blockedDay = BlockedDay.builder()
                    .id(1L)
                    .blockedDate(LocalDate.of(2026, 1, 1))
                    .dayOfWeek("THU")
                    .blockType(blockType)
                    .reason("Reason for " + blockType.name())
                    .hoursLost(BigDecimal.valueOf(3.5))
                    .build();

            BlockedDayDTO dto = mapper.toDto(blockedDay);

            assertNotNull(dto);
            assertEquals(blockType, dto.getBlockType());
        }
    }

    @Test
    void toDto_shouldPreserveExactDecimalValues() {
        BlockedDay blockedDay = BlockedDay.builder()
                .id(1L)
                .blockedDate(LocalDate.of(2026, 6, 1))
                .dayOfWeek("MON")
                .blockType(BlockType.HOLIDAY)
                .reason("Dia Internacional da Crianca")
                .hoursLost(new BigDecimal("1.5"))
                .build();

        BlockedDayDTO dto = mapper.toDto(blockedDay);

        assertNotNull(dto);
        assertEquals(new BigDecimal("1.5"), dto.getHoursLost());
    }
}
