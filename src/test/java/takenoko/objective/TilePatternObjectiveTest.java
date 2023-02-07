package takenoko.objective;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.objective.Objective;
import takenoko.game.objective.TilePatternObjective;
import takenoko.game.tile.*;
import takenoko.utils.Coord;

class TilePatternObjectiveTest {
    // The board always apply this action first
    static final Action.PlaceTile INITIAL_ACTION =
            new Action.PlaceTile(new Coord(0, 0), TileDeck.DEFAULT_DRAW_PREDICATE);

    Action.PlaceTile placeBambooTile(Board board, Coord c, Color color) {
        try {
            board.placeTile(c, new BambooTile(color));
            // irrigate all sides if possible
            for (var side : TileSide.values()) {
                try {
                    board.placeIrrigation(c, side);
                } catch (IrrigationException e) {
                    // ignore
                }
            }
        } catch (Exception e) {
            fail(e);
        }
        return new Action.PlaceTile(c, TileDeck.DEFAULT_DRAW_PREDICATE);
    }

    Action.PlaceTile placeBambooTile(Board board, Coord c) {
        return placeBambooTile(board, c, Color.GREEN);
    }

    Board prepareBoard(Objective obj, Coord... coords) {
        var board = new Board();
        assertFalse(obj.computeAchieved(board, INITIAL_ACTION, null).achieved());

        for (var c : coords) {
            var action = placeBambooTile(board, c);
            assertFalse(obj.computeAchieved(board, action, null).achieved());
        }
        return board;
    }

    @Test
    void testTriangle() {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.TRIANGLE);

        var board = prepareBoard(objective, new Coord(0, 1), new Coord(-1, 1));

        var lastAction = placeBambooTile(board, new Coord(-1, 2));
        assertTrue(objective.computeAchieved(board, lastAction, null).achieved());
    }

    @Test
    void testDiamondWithoutRightPart() throws IrrigationException, BoardException {
        var objective =
                new TilePatternObjective(
                        Color.GREEN, TilePatternObjective.DIAMOND_WITHOUT_RIGHT_PART);

        var board = new Board();
        board.placeTile(new Coord(-1, 1), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(0, 1), new BambooTile(Color.PINK));
        board.placeTile(new Coord(-1, 2), new BambooTile(Color.GREEN));

        var lastAction = placeBambooTile(board, new Coord(0, 2));
        assertTrue(objective.computeAchieved(board, lastAction, null).achieved());
    }

    @Test
    void testLineOfThree() {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3);

        // to achieve a line of 3, we need to place more than 3 tiles due to requirements
        var board = prepareBoard(objective, new Coord(1, 0), new Coord(0, 1), new Coord(-1, 1));

        var lastAction = placeBambooTile(board, new Coord(-1, 2));
        assertTrue(objective.computeAchieved(board, lastAction, null).achieved());
    }

    @Test
    void testDiamond() {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.DIAMOND);

        var board = prepareBoard(objective, new Coord(0, 1), new Coord(-1, 1), new Coord(-1, 2));

        var lastAction = placeBambooTile(board, new Coord(0, 2));
        assertTrue(objective.computeAchieved(board, lastAction, null).achieved());
    }

    @Test
    void testTwoColouredObjectiveProgression() {
        List<Color> colors = List.of(Color.YELLOW, Color.YELLOW, Color.PINK, Color.PINK);
        var objective = new TilePatternObjective(colors, TilePatternObjective.DIAMOND, 1);

        var board = new Board();

        var action = placeBambooTile(board, new Coord(0, 1), Color.YELLOW);
        assertEquals(new Objective.Status(1, 4), objective.computeAchieved(board, action, null));

        action = placeBambooTile(board, new Coord(-1, 1), Color.YELLOW);
        assertEquals(new Objective.Status(2, 4), objective.computeAchieved(board, action, null));

        action = placeBambooTile(board, new Coord(-1, 2), Color.PINK);
        assertEquals(new Objective.Status(3, 4), objective.computeAchieved(board, action, null));

        // placing an unrelated tile doesn't change the status (keeping the previous action)
        placeBambooTile(board, new Coord(1, -1), Color.YELLOW);
        assertEquals(new Objective.Status(3, 4), objective.computeAchieved(board, action, null));

        action = placeBambooTile(board, new Coord(0, 2), Color.PINK);
        assertEquals(new Objective.Status(4, 4), objective.computeAchieved(board, action, null));
    }

    @Test
    void patternScanOnlyTriggeredByPlaceTileAction() {
        // true as soon as a tile is placed
        var objective = new TilePatternObjective(Color.GREEN, List.of(new Coord(0, 0)));
        var board = new Board();
        var action = placeBambooTile(board, new Coord(0, 1));

        assertFalse(objective.computeAchieved(board, Action.NONE, null).achieved());
        assertTrue(objective.computeAchieved(board, action, null).achieved());
        // but once the objective is achieved, it stays achieved
        assertTrue(objective.computeAchieved(board, Action.NONE, null).achieved());
    }

    @Test
    void testDifferentColor() throws IrrigationException, BoardException {
        var objective = new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_2);

        var board = prepareBoard(objective, new Coord(-1, 0));

        board.placeTile(new Coord(0, -1), new BambooTile(Color.PINK));
        var action = new Action.PlaceTile(new Coord(0, -1), TileDeck.DEFAULT_DRAW_PREDICATE);

        // the objective is not achieved because the color is different
        assertFalse(objective.computeAchieved(board, action, null).achieved());

        // but if we place a green tile, it is achieved
        board.placeTile(new Coord(-1, 1), new BambooTile(Color.GREEN));
        var lastAction = new Action.PlaceTile(new Coord(-1, 1), TileDeck.DEFAULT_DRAW_PREDICATE);
        assertTrue(objective.computeAchieved(board, lastAction, null).achieved());
    }

    @Test
    void testNonIrrigated() throws IrrigationException, BoardException {
        var objective = new TilePatternObjective(Color.PINK, TilePatternObjective.LINE_2);

        // prepare board
        var board = new Board();
        board.placeTile(new Coord(-1, 0), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(0, -1), new BambooTile(Color.GREEN));

        // placing before-last tile, irrigated
        board.placeTile(new Coord(-1, -1), new BambooTile(Color.PINK));

        var beforeLastTile = (BambooTile) board.getTile(new Coord(-1, -1));
        beforeLastTile.irrigateSide(TileSide.UP_RIGHT);
        assertTrue(beforeLastTile.isIrrigated());

        // pattern not yet achieved
        var action = new Action.PlaceTile(new Coord(-1, -1), TileDeck.DEFAULT_DRAW_PREDICATE);
        assertFalse(objective.computeAchieved(board, action, null).achieved());

        // place the last tile, not irrigated
        board.placeTile(new Coord(-2, 0), new BambooTile(Color.PINK));
        var lastAction = new Action.PlaceTile(new Coord(-2, 0), TileDeck.DEFAULT_DRAW_PREDICATE);
        var lastTile = (BambooTile) board.getTile(new Coord(-2, 0));
        assertFalse(lastTile.isIrrigated());

        assertFalse(objective.computeAchieved(board, action, null).achieved());

        // but if we irrigate it, it is achieved
        lastTile.irrigateSide(TileSide.UP_LEFT);
        assertTrue(lastTile.isIrrigated());

        assertTrue(objective.computeAchieved(board, lastAction, null).achieved());
    }
}
