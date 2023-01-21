package takenoko.action;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.tile.*;
import takenoko.player.Inventory;
import takenoko.utils.Coord;

class PossibleActionListerTest {
    ActionValidator validator;
    Inventory inventory;
    Board board;
    TileDeck deck;

    @BeforeEach
    void setUp() {
        board = new Board();
        deck = new TileDeck(new Random(0));
        GameInventory gameInventory = new GameInventory(20, deck);
        inventory = new Inventory();
        validator = new ActionValidator(board, gameInventory, inventory);
    }

    @Test
    void listActionsWhenfirstAction() {
        PossibleActionLister lister = new PossibleActionLister(board, validator, inventory);

        var TILE_PRED = TileDeck.DEFAULT_DRAW_PREDICATE;

        var expected =
                List.of(
                        Action.NONE,
                        Action.END_TURN,
                        new Action.TakeBambooSizeObjective(),
                        new Action.TakeHarvestingObjective(),
                        new Action.TakeTilePatternObjective(),

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
    void listActionsWhenATileWasPlaced() throws IrrigationException, BoardException {
        PossibleActionLister lister = new PossibleActionLister(board, validator, inventory);

        var TILE_PRED = TileDeck.DEFAULT_DRAW_PREDICATE;

        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        inventory.incrementIrrigation(); // we assume that the player has an irrigation stick

        var expected =
                List.of(
                        Action.NONE,
                        Action.END_TURN,
                        new Action.TakeBambooSizeObjective(),
                        new Action.TakeHarvestingObjective(),
                        new Action.TakeTilePatternObjective(),
                        new Action.MoveGardener(new Coord(0, 1)),
                        new Action.MovePanda(new Coord(0, 1)),
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
