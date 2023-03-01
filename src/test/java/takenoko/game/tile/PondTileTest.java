package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PondTileTest {
    @Test
    void isSideIrrigated() {
        PondTile pondTile = new PondTile();
        assertTrue(pondTile.isSideIrrigated(TileSide.UP));
    }

    @Test
    void irrigateSide() {
        PondTile pondTile = new PondTile();
        assertThrows(Exception.class, () -> pondTile.irrigateSide(TileSide.UP));
    }
}
