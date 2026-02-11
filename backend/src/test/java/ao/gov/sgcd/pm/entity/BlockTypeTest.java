package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTypeTest {

    @Test
    void values_shouldReturnAllValues() {
        assertEquals(2, BlockType.values().length);
    }

    @Test
    void valueOf_HOLIDAY() {
        assertEquals(BlockType.HOLIDAY, BlockType.valueOf("HOLIDAY"));
    }

    @Test
    void valueOf_SCC_EVENT() {
        assertEquals(BlockType.SCC_EVENT, BlockType.valueOf("SCC_EVENT"));
    }

    @Test
    void valueOf_invalid_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> BlockType.valueOf("INVALID"));
    }

    @Test
    void values_shouldContainAllExpectedValues() {
        BlockType[] values = BlockType.values();
        assertEquals(BlockType.HOLIDAY, values[0]);
        assertEquals(BlockType.SCC_EVENT, values[1]);
    }

    @Test
    void ordinal_shouldMatchDeclarationOrder() {
        assertEquals(0, BlockType.HOLIDAY.ordinal());
        assertEquals(1, BlockType.SCC_EVENT.ordinal());
    }

    @Test
    void name_shouldReturnStringRepresentation() {
        assertEquals("HOLIDAY", BlockType.HOLIDAY.name());
        assertEquals("SCC_EVENT", BlockType.SCC_EVENT.name());
    }
}
