package takenoko;

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
        assertTrue(bambooTile.isCultivable());
    }
}
