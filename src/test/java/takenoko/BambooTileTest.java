package takenoko;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BambooTileTest {
    BambooTile bambooTile;

    @BeforeEach
    void setUp() {
        bambooTile = new BambooTile();
    }

    @Test
    void isCultivableTest() {
        assertTrue(bambooTile.isCultivable);
    }

    @Test
    void irrigatedSidesTest() {
        assertFalse(bambooTile.irrigatedSides[0]);
        assertFalse(bambooTile.irrigatedSides[1]);
        assertFalse(bambooTile.irrigatedSides[2]);
        assertFalse(bambooTile.irrigatedSides[3]);
        assertFalse(bambooTile.irrigatedSides[4]);
        assertFalse(bambooTile.irrigatedSides[5]);
    }
}
