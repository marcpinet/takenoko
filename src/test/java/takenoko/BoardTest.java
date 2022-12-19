package takenoko;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

    Board tileboard;

    @BeforeEach
    void setUp() {
        tileboard = new Board();
    }

    @Test
    void placeTileTest() throws Exception {
        Coord c2 = new Coord(0, 1);
        Tile t = new BambooTile();
        tileboard.placeTile(c2, t);
        assertEquals(tileboard.getTile(c2), t);
    }

    @Test
    void contains() throws Exception {
        Coord c = new Coord(0, 1);
        assertFalse(tileboard.contains(c));
        tileboard.placeTile(c, new BambooTile());
        assertTrue(tileboard.contains(c));
    }

    @Test
    void placeIrrigationTest() throws Exception {
        Coord c = new Coord(0, 1);
        Tile t = new BambooTile();
        tileboard.placeTile(c, t);
        assertTrue(tileboard.getTile(c).isSideIrrigated(TileSide.UP));
        assertThrows(IrrigationException.class, () -> tileboard.placeIrrigation(c, TileSide.UP));
        tileboard.placeIrrigation(c, TileSide.UP_LEFT);
        assertTrue(tileboard.getTile(c).isSideIrrigated(TileSide.UP_LEFT));
    }

    @Test
    void canNotPlaceIrrigationTest() {
        Coord c = new Coord(1, 2);
        assertThrows(IrrigationException.class, () -> tileboard.placeIrrigation(c, TileSide.UP));
    }
}
