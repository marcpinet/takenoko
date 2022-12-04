package takenoko;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

    Board tileboard;

    @BeforeEach
    void setUp() {
        tileboard = new Board();
    }

    @Test
    void PlaceTileTest() {
        Coord c = new Coord(1, 2);
        Coord c2 = new Coord(0, 1);
        Tile t = new BambooTile();
        tileboard.placeTile(c, t);
        assert (tileboard.getTile(c) == null);
        tileboard.placeTile(c2, t);
        assertTrue(tileboard.getTile(c2).equals(t));
    }
}
