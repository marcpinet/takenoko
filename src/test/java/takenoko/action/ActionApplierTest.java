package takenoko.action;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.Objective;
import takenoko.game.tile.*;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PrivateInventory;
import takenoko.player.bot.DefaultBot;
import takenoko.utils.Coord;
import utils.TestLogHandler;

class ActionApplierTest {
    private TestLogHandler logHandler;

    private ActionApplier applier;
    private Board board;
    private GameInventory gameInventory;
    private TileDeck deck;
    private Player player;

    void assertNoSevereLog() {
        assertFalse(
                logHandler.getRecords().stream()
                        .anyMatch(r -> r.getLevel().equals(java.util.logging.Level.SEVERE)));
    }

    @BeforeEach
    void setUp() {
        Logger logger = Logger.getGlobal();
        logHandler = new TestLogHandler();
        logger.addHandler(logHandler);

        board = new Board();
        deck = new TileDeck(new Random(0));
        gameInventory = new GameInventory(1, deck);

        applier = new ActionApplier(board, logger, gameInventory, new PrivateInventory());

        player = new DefaultBot();
    }

    @Test
    void doNothing() {
        applier.apply(Action.NONE, player);
        applier.apply(Action.END_TURN, player);
        assertNoSevereLog();
    }

    @Test
    void placeTile() throws BoardException {
        var action = new Action.PlaceTile(new Coord(0, 1), TileDeck.DEFAULT_DRAW_PREDICATE);
        int originalDeckSize = deck.size();
        applier.apply(action, player);

        var tile = board.getTile(new Coord(0, 1));
        assertTrue(tile instanceof BambooTile);
        assertEquals(originalDeckSize - 1, deck.size());
        assertNoSevereLog();
    }

    @Test
    void unveilObjective() throws InventoryException {
        var mockObj = mock(Objective.class);
        when(mockObj.isAchieved(any(), any(), any())).thenReturn(true);
        when(mockObj.wasAchievedAfterLastCheck()).thenReturn(true);
        when(mockObj.getScore()).thenReturn(1);

        player.getPrivateInventory().addObjective(mockObj);

        var action = new Action.UnveilObjective(mockObj);
        applier.apply(action, player);

        assertEquals(mockObj.getScore(), player.getScore());

        assertNoSevereLog();
    }

    @Test
    void unveilHarvestingObjective() throws InventoryException {
        var visibleInv = player.getVisibleInventory();
        var privateInv = player.getPrivateInventory();
        visibleInv.incrementBamboo(Color.GREEN);
        visibleInv.incrementBamboo(Color.PINK);
        visibleInv.incrementBamboo(Color.YELLOW);

        var mockObj = mock(HarvestingObjective.class);
        when(mockObj.isAchieved(any(), any(), any())).thenReturn(true);
        when(mockObj.wasAchievedAfterLastCheck()).thenReturn(true);
        when(mockObj.getGreen()).thenReturn(1);
        when(mockObj.getPink()).thenReturn(1);
        when(mockObj.getYellow()).thenReturn(1);

        privateInv.addObjective(mockObj);

        var action = new Action.UnveilObjective(mockObj);
        applier.apply(action, player);

        assertEquals(0, visibleInv.getBamboo(Color.GREEN));
        assertEquals(0, visibleInv.getBamboo(Color.PINK));
        assertEquals(0, visibleInv.getBamboo(Color.YELLOW));

        assertEquals(mockObj.getScore(), player.getScore());

        assertNoSevereLog();
    }

    @Test
    void placeIrrigation() throws BoardException, IrrigationException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        player.getVisibleInventory().incrementIrrigation();

        var tile = board.getTile(new Coord(0, 1));
        var bambooTile = (BambooTile) tile;
        assertFalse(bambooTile.isSideIrrigated(TileSide.UP_RIGHT));

        var action = new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.UP_RIGHT);
        applier.apply(action, player);

        assertTrue(bambooTile.isSideIrrigated(TileSide.UP_RIGHT));
        assertFalse(player.getVisibleInventory().hasIrrigation());
        assertNoSevereLog();
    }

    @Test
    void takeIrrigation() {
        assertFalse(player.getVisibleInventory().hasIrrigation());
        assertTrue(gameInventory.hasIrrigation());

        var action = new Action.TakeIrrigationStick();
        applier.apply(action, player);

        assertTrue(player.getVisibleInventory().hasIrrigation());
        assertFalse(gameInventory.hasIrrigation());
        assertNoSevereLog();
    }

    @Test
    void movePanda() throws IrrigationException, BoardException {
        var c = new Coord(0, 1);
        board.placeTile(c, new BambooTile(Color.GREEN));

        var action = new Action.MovePanda(c);
        applier.apply(action, player);

        assertEquals(c, board.getPandaCoord());
        assertNoSevereLog();
    }

    @Test
    void moveGardener() throws IrrigationException, BoardException {
        var c = new Coord(0, 1);
        board.placeTile(c, new BambooTile(Color.GREEN));

        var action = new Action.MoveGardener(c);
        applier.apply(action, player);

        assertEquals(c, board.getGardenerCoord());
        assertNoSevereLog();
    }
}
