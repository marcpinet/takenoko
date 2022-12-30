package takenoko.game.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.tile.*;
import takenoko.utils.Coord;

public class BoardTest {

    Board tileboard;

    @BeforeEach
    void setUp() {
        tileboard = new Board();
    }

    @Test
    void placeTileTest() throws Exception {
        Coord c2 = new Coord(0, 1);
        Tile t = new BambooTile(Color.GREEN);
        tileboard.placeTile(c2, t);
        assertEquals(tileboard.getTile(c2), t);
    }

    @Test
    void contains() throws Exception {
        Coord c = new Coord(0, 1);
        assertFalse(tileboard.contains(c));
        tileboard.placeTile(c, new BambooTile(Color.GREEN));
        assertTrue(tileboard.contains(c));
    }

    @Test
    void placeIrrigationTest() throws Exception {
        Coord c = new Coord(0, 1);
        Tile t = new BambooTile(Color.GREEN);
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

    @Test
    void moveTest() throws Exception {
        Coord c1 = new Coord(0, 1);
        Coord c2 = new Coord(0, 2);
        Tile t1 = new BambooTile(Color.GREEN);
        tileboard.placeTile(c1, t1);
        tileboard.placeTile(new Coord(1, 0), new BambooTile(Color.GREEN));
        // Gardener
        tileboard.move(MovablePiece.GARDENER, c1);
        assertEquals(tileboard.getGardenerCoord(), c1);
        assertThrows(BoardException.class, () -> tileboard.move(MovablePiece.GARDENER, c2));
        Coord c3 = new Coord(1, 1);
        Tile t2 = new BambooTile(Color.GREEN);
        tileboard.placeTile(c3, t2);
        assertThrows(
                BambooIrrigationException.class, () -> tileboard.move(MovablePiece.GARDENER, c3));
        tileboard.placeIrrigation(c3, TileSide.UP_LEFT);
        assertDoesNotThrow(() -> tileboard.move(MovablePiece.GARDENER, c3));
        // Panda
        assertDoesNotThrow(() -> tileboard.move(MovablePiece.PANDA, c1));
        assertEquals(tileboard.getPandaCoord(), c1);
        assertThrows(BoardException.class, () -> tileboard.move(MovablePiece.PANDA, c2));
        Coord c4 = new Coord(1, 2);
        Tile t3 = new BambooTile(Color.GREEN);
        tileboard.placeTile(c2, new BambooTile(Color.GREEN));
        tileboard.placeTile(c4, t3);
        assertThrows(BambooSizeException.class, () -> tileboard.move(MovablePiece.PANDA, c4));
    }
}
