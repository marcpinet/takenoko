package takenoko;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PondTileTest {

    @Test
    void isCultivableTest() {
        PondTile pondTile = new PondTile();
        assertFalse(pondTile.isCultivable);
    }

    @Test
    void irrigatedSidesTest() {
        PondTile pondTile = new PondTile();
        assertTrue(pondTile.irrigatedSides[0]);
        assertTrue(pondTile.irrigatedSides[1]);
        assertTrue(pondTile.irrigatedSides[2]);
        assertTrue(pondTile.irrigatedSides[3]);
        assertTrue(pondTile.irrigatedSides[4]);
        assertTrue(pondTile.irrigatedSides[5]);
    }
}
