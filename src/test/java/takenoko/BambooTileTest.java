package takenoko;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BambooTileTest {
    BambooTile bambooTile;

    @BeforeEach
    void setUp() {
        bambooTile = new BambooTile();
    }

    @Test
    void testGrowBamboo() {
        assertEquals(0, bambooTile.getBambooSize());
        bambooTile.growBamboo();
        assertEquals(1, bambooTile.getBambooSize());
        bambooTile.growBamboo();
        assertEquals(2, bambooTile.getBambooSize());
        bambooTile.growBamboo();
        assertEquals(3, bambooTile.getBambooSize());
        bambooTile.growBamboo();
        assertEquals(4, bambooTile.getBambooSize());
        bambooTile.growBamboo();
        assertEquals(4, bambooTile.getBambooSize());
    }

    @Test
    void testShrinkBamboo() {
        assertEquals(0, bambooTile.getBambooSize());
        bambooTile.shrinkBamboo();
        assertEquals(0, bambooTile.getBambooSize());
        bambooTile.growBamboo();
        bambooTile.growBamboo();
        assertEquals(2, bambooTile.getBambooSize());
        bambooTile.shrinkBamboo();
        assertEquals(1, bambooTile.getBambooSize());
        bambooTile.shrinkBamboo();
        assertEquals(0, bambooTile.getBambooSize());
    }

    @Test
    void testIsCultivable() {
        assertTrue(bambooTile.isCultivable());
        bambooTile.growBamboo();
        bambooTile.growBamboo();
        bambooTile.growBamboo();
        assertTrue(bambooTile.isCultivable());
        bambooTile.growBamboo();
        assertFalse(bambooTile.isCultivable());
        bambooTile.shrinkBamboo();
        assertTrue(bambooTile.isCultivable());
    }

    @Test
    void testEquals() {
        BambooTile otherTile = new BambooTile();
        assertEquals(bambooTile, otherTile);
        bambooTile.growBamboo();
        assertNotEquals(bambooTile, otherTile);
        otherTile.growBamboo();
        assertEquals(bambooTile, otherTile);
        otherTile.growBamboo();
        assertNotEquals(bambooTile, otherTile);
    }
}
