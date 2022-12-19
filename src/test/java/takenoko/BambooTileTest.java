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
    void testGrowBamboo() throws BambooSizeException {
        assertEquals(0, bambooTile.getBambooSize());
        bambooTile.irrigateSide(TileSide.UP);
        bambooTile.growBamboo();
        assertEquals(1, bambooTile.getBambooSize());

        for (int i = 0; i < 3; i++) {
            bambooTile.growBamboo();
        }

        assertEquals(4, bambooTile.getBambooSize());
        assertThrows(BambooSizeException.class, () -> bambooTile.growBamboo());
    }

    @Test
    void testShrinkBamboo() throws BambooSizeException {
        assertThrows(BambooSizeException.class, () -> bambooTile.shrinkBamboo());
        bambooTile.irrigateSide(TileSide.UP);
        bambooTile.growBamboo();
        assertEquals(1, bambooTile.getBambooSize());
        bambooTile.shrinkBamboo();
        assertEquals(0, bambooTile.getBambooSize());
    }

    @Test
    void testIsCultivable() throws BambooSizeException {
        assertTrue(bambooTile.isCultivable());
        bambooTile.irrigateSide(TileSide.UP);
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
    void testEquals() throws BambooSizeException {
        BambooTile otherTile = new BambooTile();
        assertEquals(bambooTile, otherTile);
        bambooTile.irrigateSide(TileSide.UP);
        otherTile.irrigateSide(TileSide.UP);
        bambooTile.growBamboo();
        assertNotEquals(bambooTile, otherTile);
        otherTile.growBamboo();
        assertEquals(bambooTile, otherTile);
        otherTile.growBamboo();
        assertNotEquals(bambooTile, otherTile);
    }

    @Test
    void irrigateSideTest() {
        bambooTile.irrigateSide(TileSide.UP);
        assertTrue(bambooTile.isSideIrrigated(TileSide.UP));
    }

    @Test
    void powerUpFertilizerTest() throws Exception {
        bambooTile.irrigateSide(TileSide.UP);
        bambooTile.setPowerUp(PowerUp.FERTILIZER);
        bambooTile.growBamboo();
        assertEquals(2, bambooTile.getBambooSize());
    }

    @Test
    void powerUpWatershedTest() throws PowerUpException {
        bambooTile.setPowerUp(PowerUp.WATERSHED);
        assertDoesNotThrow(() -> bambooTile.growBamboo());
    }
}
