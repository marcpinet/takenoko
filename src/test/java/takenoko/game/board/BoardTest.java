package takenoko.game.board;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.tile.*;
import takenoko.player.Player;
import takenoko.player.bot.RandomBot;
import takenoko.utils.Coord;

class BoardTest {

    Player p1, p2;
    Board board;

    @BeforeEach
    void setUp() {
        p1 = new RandomBot(new Random(), "edgar");
        p2 = new RandomBot(new Random(), "marc");
        board = new Board(List.of(p1, p2));
    }

    @Test
    void placeTile() throws Exception {
        Coord c2 = new Coord(0, 1);
        Tile t = new BambooTile(Color.GREEN);
        board.placeTile(c2, t);
        assertEquals(board.getTile(c2), t);
    }

    @Test
    void placeTileAdjacentToTwo() throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 0), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 1), new BambooTile(Color.GREEN));
        assertEquals(board.getTile(new Coord(1, 1)), new BambooTile(Color.GREEN));
    }

    @Test
    void cannotPlaceTile() throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));

        // Must be adjacent to the pond or TWO tiles
        var c = new Coord(0, 2);
        var t = new BambooTile(Color.GREEN);

        assertThrows(Exception.class, () -> board.placeTile(c, t));
    }

    @Test
    void contains() throws Exception {
        Coord c = new Coord(0, 1);
        assertFalse(board.contains(c));
        board.placeTile(c, new BambooTile(Color.GREEN));
        assertTrue(board.contains(c));
    }

    @Test
    void placeIrrigation() throws Exception {
        Coord c = new Coord(0, 1);
        Tile t = new BambooTile(Color.GREEN);
        board.placeTile(c, t);
        assertTrue(board.getTile(c).isSideIrrigated(TileSide.UP));
        assertThrows(IrrigationException.class, () -> board.placeIrrigation(c, TileSide.UP));
        board.placeIrrigation(c, TileSide.UP_LEFT);
        assertTrue(board.getTile(c).isSideIrrigated(TileSide.UP_LEFT));
    }

    @Test
    void canNotPlaceIrrigation() {
        Coord c = new Coord(1, 2);
        assertThrows(BoardException.class, () -> board.placeIrrigation(c, TileSide.UP));
    }

    @Test
    void move() throws Exception {
        Coord c1 = new Coord(0, 1);
        Coord c2 = new Coord(0, 2);
        BambooTile t1 = new BambooTile(Color.GREEN);
        board.placeTile(c1, t1);
        t1.growBamboo();
        board.placeTile(new Coord(1, 0), new BambooTile(Color.GREEN));
        // Gardener
        board.move(MovablePiece.GARDENER, c1, p1);
        assertEquals(board.getPieceCoord(MovablePiece.GARDENER), c1);
        assertThrows(BoardException.class, () -> board.move(MovablePiece.GARDENER, c2, p1));
        Coord c3 = new Coord(1, 1);
        Tile t2 = new BambooTile(Color.GREEN);
        board.placeTile(c3, t2);
        board.placeIrrigation(c3, TileSide.UP_LEFT);
        board.move(MovablePiece.GARDENER, c3, p1);
        // Panda
        assertEquals(0, p1.getVisibleInventory().getBamboo(Color.GREEN));
        board.move(MovablePiece.PANDA, c1, p1);
        assertEquals(board.getPieceCoord(MovablePiece.PANDA), c1);
        assertEquals(1, p1.getVisibleInventory().getBamboo(Color.GREEN));
        assertThrows(BoardException.class, () -> board.move(MovablePiece.PANDA, c2, p1));
        assertEquals(1, p1.getVisibleInventory().getBamboo(Color.GREEN));
        Coord c4 = new Coord(1, 2);
        Tile t3 = new BambooTile(Color.GREEN);
        board.placeTile(c2, new BambooTile(Color.GREEN));
        board.placeTile(c4, t3);
        assertThrows(BoardException.class, () -> board.move(MovablePiece.PANDA, c4, p1));
    }
}
