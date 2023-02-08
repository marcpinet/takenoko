package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BambooTileTest {
    BambooTile bambooTile;

    @BeforeEach
    void setUp() {
        bambooTile = new BambooTile(Color.GREEN);
    }

    @Test
    void growBamboo() throws BambooSizeException, BambooIrrigationException {
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
    void shrinkBamboo() throws BambooSizeException, BambooIrrigationException {
        assertThrows(BambooSizeException.class, () -> bambooTile.shrinkBamboo());
        bambooTile.irrigateSide(TileSide.UP);
        bambooTile.growBamboo();
        assertEquals(1, bambooTile.getBambooSize());
        bambooTile.shrinkBamboo();
        assertEquals(0, bambooTile.getBambooSize());
    }

    @Test
    void isCultivable() throws BambooSizeException, BambooIrrigationException {
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
    void equals() throws BambooSizeException, BambooIrrigationException {
        BambooTile otherTile = new BambooTile(Color.GREEN);
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
    void irrigateSide() {
        bambooTile.irrigateSide(TileSide.UP);
        assertTrue(bambooTile.isSideIrrigated(TileSide.UP));
    }

    @Test
    void powerUpFertilizer() throws Exception {
        bambooTile.irrigateSide(TileSide.UP);
        bambooTile.growBamboo();
        bambooTile.setPowerUp(PowerUp.FERTILIZER);
        bambooTile.growBamboo();
        assertEquals(3, bambooTile.getBambooSize());

        bambooTile.growBamboo();
        assertEquals(4, bambooTile.getBambooSize());
    }

    @Test
    void powerUpWatershed() throws PowerUpException {
        bambooTile.setPowerUp(PowerUp.WATERSHED);
        assertDoesNotThrow(() -> bambooTile.growBamboo());
    }

    @Test
    void cannotSetTwoPowerUps() throws PowerUpException {
        bambooTile.setPowerUp(PowerUp.WATERSHED);
        assertThrows(PowerUpException.class, () -> bambooTile.setPowerUp(PowerUp.FERTILIZER));
    }

    @Test
    void canRemovePowerUp() throws PowerUpException {
        bambooTile.setPowerUp(PowerUp.WATERSHED);
        bambooTile.setPowerUp(PowerUp.NONE);
        assertDoesNotThrow(() -> bambooTile.setPowerUp(PowerUp.FERTILIZER));
    }
}
