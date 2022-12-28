package takenoko.objective;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import takenoko.*;

class TilePatternObjectiveTest {
    // The board always apply this action first
    static final Action.PlaceTile INITIAL_ACTION =
            new Action.PlaceTile(new Coord(0, 0), TileDeck.DEFAULT_DRAW_TILE_PREDICATE);

    Action.PlaceTile placeBambooTile(Board board, Coord c) {
        try {
            board.placeTile(c, new BambooTile());
        } catch (Exception e) {
            fail(e);
        }
        return new Action.PlaceTile(c, TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
    }

    @Test
    void testLineOfTwo() {
        var objective = new TilePatternObjective(TilePatternObjective.LINE_2);

        var board = new Board();
        assertFalse(objective.isAchieved(board, INITIAL_ACTION));

        // place the second tile
        var secondAction = placeBambooTile(board, new Coord(0, 1));

        // Now objective is achieved
        assertTrue(objective.isAchieved(board, secondAction));
    }

    @Test
    void testLineOfThree() {
        var objective = new TilePatternObjective(TilePatternObjective.LINE_3);

        var board = new Board();
        assertFalse(objective.isAchieved(board, INITIAL_ACTION));

        // place the second tile
        var secondAction = placeBambooTile(board, new Coord(0, 1));
        assertFalse(objective.isAchieved(board, secondAction));

        // place the third tile
        var thirdAction = placeBambooTile(board, new Coord(0, 2));

        // Now objective is achieved
        assertTrue(objective.isAchieved(board, thirdAction));
    }

    @Test
    void testSquareOfTwo() {
        var objective = new TilePatternObjective(TilePatternObjective.DIAMOND_4);

        var board = new Board();
        assertFalse(objective.isAchieved(board, INITIAL_ACTION));

        // Place the other tiles
        var secondAction = placeBambooTile(board, new Coord(0, 1));
        assertFalse(objective.isAchieved(board, secondAction));
        // On purpose rotated
        var thirdAction = placeBambooTile(board, new Coord(1, 0));

        assertFalse(objective.isAchieved(board, thirdAction));
        var fourthAction = placeBambooTile(board, new Coord(-1, +1));
        assertTrue(objective.isAchieved(board, fourthAction));
    }

    @Test
    void testTriangleOfTwo() {
        var objective = new TilePatternObjective(TilePatternObjective.TRIANGLE_3);

        var board = new Board();
        assertFalse(objective.isAchieved(board, INITIAL_ACTION));

        // Place the other tiles
        var secondAction = placeBambooTile(board, new Coord(0, 1));
        assertFalse(objective.isAchieved(board, secondAction));
        var thirdAction = placeBambooTile(board, new Coord(1, 0));

        // Now objective is achieved
        assertTrue(objective.isAchieved(board, thirdAction));
    }

    @Test
    void patternScanOnlyTriggeredByPlaceTileAction() {
        // always true
        var objective = new TilePatternObjective(List.of(new Coord(0, 0)));
        var board = new Board();
        assertFalse(objective.isAchieved(board, Action.NONE));
        assertTrue(objective.isAchieved(board, INITIAL_ACTION));
        // but once the objective is achieved, it stays achieved
        assertTrue(objective.isAchieved(board, Action.NONE));
    }
}
