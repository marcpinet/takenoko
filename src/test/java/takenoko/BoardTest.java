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
        Tile t = new BambooTile();
        tileboard.placeTile(c, t);
        assertTrue(tileboard.getTiles().containsKey(c));
        assertTrue(tileboard.getTiles().containsValue(t));
        assertTrue(tileboard.getTiles().get(c).equals(t));
    }
}
