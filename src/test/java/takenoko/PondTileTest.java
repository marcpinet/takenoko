package takenoko;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class PondTileTest {

    @Test
    void isCultivableTest() {
        PondTile pondTile = new PondTile();
        assertFalse(pondTile.isCultivable());
    }
}
