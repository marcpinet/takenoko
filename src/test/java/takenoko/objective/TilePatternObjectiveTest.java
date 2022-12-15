package takenoko.objective;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import takenoko.*;

class TilePatternObjectiveTest {
    // The board always apply this action first
    static final Action.PlaceTile INITIAL_ACTION =
            new Action.PlaceTile(new Coord(0, 0), new PondTile());

    Action.PlaceTile placeBambooTile(Board board, Coord c) {
        try {
            board.placeTile(c, new BambooTile());
        } catch (BoardException e) {
            fail(e);
        }
        return new Action.PlaceTile(c, new BambooTile());
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
    void testSquareOfTwo() {
        var objective = new TilePatternObjective(TilePatternObjective.SQUARE_2x2);

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
}
