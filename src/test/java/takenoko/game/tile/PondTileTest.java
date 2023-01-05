package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PondTileTest {

    @Test
    void isCultivableTest() {
        PondTile pondTile = new PondTile();
        assertFalse(pondTile.isCultivable());
    }

    @Test
    void isSideIrrigatedTest() {
        PondTile pondTile = new PondTile();
        assertTrue(pondTile.isSideIrrigated(TileSide.UP));
    }

    @Test
    void irrigateSideTest() {
        PondTile pondTile = new PondTile();
        assertThrows(Exception.class, () -> pondTile.irrigateSide(TileSide.UP));
    }
}
