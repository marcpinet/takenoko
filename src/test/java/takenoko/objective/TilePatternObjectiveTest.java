package takenoko.objective;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.objective.TilePatternObjective;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.Color;
import takenoko.game.tile.TileDeck;
import takenoko.utils.Coord;

class TilePatternObjectiveTest {
    // The board always apply this action first
    static final Action.PlaceTile INITIAL_ACTION =
            new Action.PlaceTile(new Coord(0, 0), TileDeck.DEFAULT_DRAW_TILE_PREDICATE);

    Action.PlaceTile placeBambooTile(Board board, Coord c) {
        try {
            board.placeTile(c, new BambooTile(Color.GREEN));
        } catch (Exception e) {
            fail(e);
        }
        return new Action.PlaceTile(c, TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
    }

    @Test
    void testLineOfTwo() {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_2);

        var board = new Board();
        assertFalse(objective.isAchieved(board, INITIAL_ACTION));

        // place the second tile
        var secondAction = placeBambooTile(board, new Coord(0, 1));

        // Now objective is achieved
        assertTrue(objective.isAchieved(board, secondAction));
    }

    @Test
    void testLineOfThree() {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3);

        var board = new Board();
        assertFalse(objective.isAchieved(board, INITIAL_ACTION));

        // place the second tile
        var secondAction = placeBambooTile(board, new Coord(0, 1));
        assertFalse(objective.isAchieved(board, secondAction));

        // Placing tiles, so we can place them without raising error adjacent
        placeBambooTile(board, new Coord(1, 0));
        placeBambooTile(board, new Coord(1, 1));

        // place the third tile
        var thirdAction = placeBambooTile(board, new Coord(0, 2));

        // Now objective is achieved
        assertTrue(objective.isAchieved(board, thirdAction));
    }

    @Test
    void testSquareOfTwo() {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.DIAMOND_4);

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
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.TRIANGLE_3);

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
        var objective = new TilePatternObjective(Color.GREEN, List.of(new Coord(0, 0)));
        var board = new Board();
        assertFalse(objective.isAchieved(board, Action.NONE));
        assertTrue(objective.isAchieved(board, INITIAL_ACTION));
        // but once the objective is achieved, it stays achieved
        assertTrue(objective.isAchieved(board, Action.NONE));
    }
}
