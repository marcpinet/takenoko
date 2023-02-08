package takenoko.player.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.action.PossibleActionLister;
import takenoko.game.GameInventory;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.objective.Objective;
import takenoko.game.objective.TilePatternObjective;
import takenoko.game.tile.*;
import takenoko.player.InventoryException;
import takenoko.utils.Coord;

class PlotRushBotTest {
    private Board board;
    private PlotRushBot bot;
    private PossibleActionLister actionLister;
    private ActionValidator validator;

    @BeforeEach
    void setUp() {
        board = new Board();
        var random = new Random(0);
        bot = new PlotRushBot(random, "marc");

        var gameInventory =
                new GameInventory(20, new TileDeck(random), random, new WeatherDice(random));
        validator =
                new ActionValidator(
                        board,
                        gameInventory,
                        bot.getPrivateInventory(),
                        bot.getVisibleInventory(),
                        WeatherDice.Face.SUN);
        actionLister = new PossibleActionLister(board, validator, bot.getPrivateInventory());
        bot.beginTurn(2);
    }

    @Test
    void rushTilePattern() {
        var action = bot.chooseAction(board, actionLister);
        var expected = new Action.TakeObjective(Objective.Type.TILE_PATTERN);

        assertEquals(expected, action);
    }

    @Test
    void simulateBestAction() throws IrrigationException, BoardException {
        var tpob = new TilePatternObjective(Color.YELLOW, TilePatternObjective.LINE_3, 3);
        try {
            for (int i = 0; i < 5; i++) bot.getPrivateInventory().addObjective(tpob);
        } catch (InventoryException ignored) {
        }

        board.placeTile(new Coord(1, 0), new BambooTile(Color.YELLOW));
        board.placeTile(new Coord(0, 1), new BambooTile(Color.YELLOW));
        board.placeTile(new Coord(1, 1), new BambooTile(Color.YELLOW));
        board.placeTile(new Coord(2, 0), new BambooTile(Color.YELLOW));
        board.placeTile(new Coord(2, 1), new BambooTile(Color.YELLOW));

        board.placeIrrigation(new Coord(1, 0), TileSide.DOWN_LEFT);
        board.placeIrrigation(new Coord(0, 1), TileSide.DOWN_RIGHT);
        board.placeIrrigation(new Coord(1, 1), TileSide.DOWN_LEFT);
        board.placeIrrigation(new Coord(2, 0), TileSide.DOWN_RIGHT);
        board.placeIrrigation(new Coord(2, 1), TileSide.DOWN_LEFT);
        board.placeIrrigation(new Coord(2, 1), TileSide.DOWN_RIGHT);

        var simulation = bot.chooseAction(board, actionLister);

        var map = ((Action.SimulateActions) (simulation)).outObjectiveStatus();
        // Manually filling map
        Map<Objective, Objective.Status> obj = new HashMap<>();
        obj.put(tpob, new Objective.Status(2, 3));
        Map<Objective, Objective.Status> obj2 = new HashMap<>();
        obj2.put(tpob, new Objective.Status(3, 3));

        map.put(new Action.PlaceTile(new Coord(0, -1), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(0, 2), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(-1, 1), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(2, -1), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(1, -1), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(-1, 0), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(2, 1), TileDeck.DEFAULT_DRAW_PREDICATE), obj);
        map.put(new Action.PlaceTile(new Coord(2, 2), TileDeck.DEFAULT_DRAW_PREDICATE), obj2);

        var action = bot.chooseAction(board, actionLister);
        var expected = new Action.PlaceTile(new Coord(2, 2), TileDeck.DEFAULT_DRAW_PREDICATE);

        assertEquals(expected, action);
    }
}
