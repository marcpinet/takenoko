package takenoko.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.GameInventory;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.Objective;
import takenoko.game.tile.*;
import takenoko.player.PrivateInventory;
import takenoko.utils.Coord;

class PossibleActionListerTest {
    ActionValidator validator;
    PrivateInventory privateInventory;
    VisibleInventory visibleInventory;
    Board board;
    TileDeck deck;

    @BeforeEach
    void setUp() {
        board = new Board();
        deck = new TileDeck(new Random(0));

        privateInventory = new PrivateInventory();
        visibleInventory = new VisibleInventory();
        resetValidator(WeatherDice.Face.SUN);
    }

    void resetValidator(WeatherDice.Face weather) {
        WeatherDice dice = mock(WeatherDice.class);
        when(dice.throwDice()).thenReturn(weather);
        GameInventory gameInventory = new GameInventory(20, deck, new Random(0), dice);
        validator =
                new ActionValidator(
                        board, gameInventory, privateInventory, visibleInventory, weather);
    }

    @Test
    void listActionsWhenFirstAction() {
        PossibleActionLister lister = new PossibleActionLister(board, validator, privateInventory);

        var TILE_PRED = TileDeck.DEFAULT_DRAW_PREDICATE;

        var expected =
                List.of(
                        new Action.TakeObjective(Objective.Type.BAMBOO_SIZE),
                        new Action.TakeObjective(Objective.Type.HARVESTING),
                        new Action.TakeObjective(Objective.Type.TILE_PATTERN),

                        // not possible to move characters on the first turn

                        new Action.TakeIrrigationStick(),

                        // not possible to place irrigation stick on the first turn

                        new Action.PlaceTile(new Coord(0, -1), TILE_PRED),
                        new Action.PlaceTile(new Coord(0, 1), TILE_PRED),
                        new Action.PlaceTile(new Coord(-1, 1), TILE_PRED),
                        new Action.PlaceTile(new Coord(1, -1), TILE_PRED),
                        new Action.PlaceTile(new Coord(-1, 0), TILE_PRED),
                        new Action.PlaceTile(new Coord(1, 0), TILE_PRED));

        var actual = lister.getPossibleActions(TILE_PRED);

        assertEquals(expected, actual);
    }

    @Test
    void listActionsWhenATileWasPlacedWithCloud() throws IrrigationException, BoardException {
        resetValidator(WeatherDice.Face.CLOUDY);
        PossibleActionLister lister = new PossibleActionLister(board, validator, privateInventory);

        var TILE_PRED = TileDeck.DEFAULT_DRAW_PREDICATE;

        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        visibleInventory.incrementIrrigation(); // we assume that the player has an irrigation stick

        var expected =
                List.of(
                        new Action.TakeObjective(Objective.Type.BAMBOO_SIZE),
                        new Action.TakeObjective(Objective.Type.HARVESTING),
                        new Action.TakeObjective(Objective.Type.TILE_PATTERN),
                        new Action.PickPowerUp(PowerUp.ENCLOSURE),
                        new Action.PickPowerUp(PowerUp.FERTILIZER),
                        new Action.PickPowerUp(PowerUp.WATERSHED),
                        new Action.MovePiece(MovablePiece.GARDENER, new Coord(0, 1)),
                        new Action.MovePiece(MovablePiece.PANDA, new Coord(0, 1)),
                        new Action.TakeIrrigationStick(),
                        new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.UP_RIGHT),
                        new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.DOWN_RIGHT),
                        new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.DOWN),
                        new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.DOWN_LEFT),
                        new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.UP_LEFT),
                        new Action.PlaceTile(new Coord(0, -1), TILE_PRED),
                        new Action.PlaceTile(new Coord(-1, 1), TILE_PRED),
                        new Action.PlaceTile(new Coord(1, -1), TILE_PRED),
                        new Action.PlaceTile(new Coord(-1, 0), TILE_PRED),
                        new Action.PlaceTile(new Coord(1, 0), TILE_PRED));

        var actual = lister.getPossibleActions(TILE_PRED);

        assertEquals(expected, actual);
    }
}
