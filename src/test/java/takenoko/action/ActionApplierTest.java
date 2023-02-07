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
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.BambooSizeObjective;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.Objective;
import takenoko.game.tile.*;
import takenoko.player.InventoryException;
import takenoko.player.Player;
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

    private UndoStack undoStack;

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
        gameInventory = new GameInventory(1, deck, new Random(0));

        player = new DefaultBot();
        applier = new ActionApplier(board, logger, gameInventory, player);

        undoStack = new UndoStack();

        applier.apply(undoStack, Action.BEGIN_SIMULATION); // allow rollback
    }

    @Test
    void doNothing() {
        applier.apply(undoStack, Action.NONE);
        applier.apply(undoStack, Action.END_TURN);

        assertNoSevereLog();
    }

    @Test
    void placeTile() throws BoardException {
        var action = new Action.PlaceTile(new Coord(0, 1), TileDeck.DEFAULT_DRAW_PREDICATE);
        int originalDeckSize = deck.size();
        applier.apply(undoStack, action);

        var tile = board.getTile(new Coord(0, 1));
        assertTrue(tile instanceof BambooTile);
        assertEquals(originalDeckSize - 1, deck.size());

        applier.apply(undoStack, Action.END_SIMULATION);

        assertThrows(BoardException.class, () -> board.getTile(new Coord(0, 1)));
        assertEquals(originalDeckSize, deck.size());

        assertNoSevereLog();
    }

    @Test
    void unveilObjective() throws InventoryException {
        var mockObj = mock(Objective.class);
        when(mockObj.computeAchieved(any(), any(), any())).thenReturn(new Objective.Status(1, 1));
        when(mockObj.isAchieved()).thenReturn(true);
        when(mockObj.getScore()).thenReturn(1);

        player.getPrivateInventory().addObjective(mockObj);

        var action = new Action.UnveilObjective(mockObj);
        assertFalse(player.getVisibleInventory().getFinishedObjectives().contains(mockObj));
        assertTrue(player.getPrivateInventory().getObjectives().contains(mockObj));

        applier.apply(undoStack, action);

        assertEquals(mockObj.getScore(), player.getScore());
        assertTrue(player.getVisibleInventory().getFinishedObjectives().contains(mockObj));
        assertFalse(player.getPrivateInventory().getObjectives().contains(mockObj));

        applier.apply(undoStack, Action.END_SIMULATION);

        assertEquals(0, player.getScore());
        assertFalse(player.getVisibleInventory().getFinishedObjectives().contains(mockObj));
        assertTrue(player.getPrivateInventory().getObjectives().contains(mockObj));

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
        when(mockObj.computeAchieved(any(), any(), any())).thenReturn(new Objective.Status(1, 1));
        when(mockObj.isAchieved()).thenReturn(true);
        when(mockObj.getGreen()).thenReturn(1);
        when(mockObj.getPink()).thenReturn(1);
        when(mockObj.getYellow()).thenReturn(1);

        privateInv.addObjective(mockObj);

        var action = new Action.UnveilObjective(mockObj);
        applier.apply(undoStack, action);

        assertEquals(0, visibleInv.getBamboo(Color.GREEN));
        assertEquals(0, visibleInv.getBamboo(Color.PINK));
        assertEquals(0, visibleInv.getBamboo(Color.YELLOW));

        assertEquals(mockObj.getScore(), player.getScore());

        applier.apply(undoStack, Action.END_SIMULATION);

        assertEquals(0, player.getScore());
        assertEquals(1, visibleInv.getBamboo(Color.GREEN));
        assertEquals(1, visibleInv.getBamboo(Color.PINK));
        assertEquals(1, visibleInv.getBamboo(Color.YELLOW));

        assertNoSevereLog();
    }

    @Test
    void placeIrrigation() throws BoardException, IrrigationException {
        Coord coord = new Coord(0, 1);
        TileSide side = TileSide.UP_RIGHT;

        board.placeTile(coord, new BambooTile(Color.GREEN));
        player.getVisibleInventory().incrementIrrigation();

        var tile = board.getTile(coord);
        var bambooTile = (BambooTile) tile;
        assertFalse(bambooTile.isSideIrrigated(side));

        var action = new Action.PlaceIrrigationStick(coord, side);
        applier.apply(undoStack, action);

        assertTrue(bambooTile.isSideIrrigated(side));
        assertFalse(player.getVisibleInventory().hasIrrigation());

        applier.apply(undoStack, Action.END_SIMULATION);

        assertFalse(board.getTile(coord).isSideIrrigated(side));
        assertTrue(player.getVisibleInventory().hasIrrigation());

        assertNoSevereLog();
    }

    @Test
    void takeIrrigation() {
        assertFalse(player.getVisibleInventory().hasIrrigation());
        assertTrue(gameInventory.hasIrrigation());

        var action = new Action.TakeIrrigationStick();
        applier.apply(undoStack, action);

        assertTrue(player.getVisibleInventory().hasIrrigation());
        assertFalse(gameInventory.hasIrrigation());

        applier.apply(undoStack, Action.END_SIMULATION);

        assertFalse(player.getVisibleInventory().hasIrrigation());
        assertTrue(gameInventory.hasIrrigation());

        assertNoSevereLog();
    }

    @Test
    void movePanda() throws IrrigationException, BoardException {
        var c = new Coord(0, 1);
        board.placeTile(c, new BambooTile(Color.GREEN));

        var action = new Action.MovePiece(MovablePiece.PANDA, c);
        applier.apply(undoStack, action);

        assertEquals(c, board.getPieceCoord(MovablePiece.PANDA));

        applier.apply(undoStack, Action.END_SIMULATION);

        assertEquals(new Coord(0, 0), board.getPieceCoord(MovablePiece.PANDA));

        assertNoSevereLog();
    }

    @Test
    void moveGardener() throws IrrigationException, BoardException {
        var c = new Coord(0, 1);
        board.placeTile(c, new BambooTile(Color.GREEN));

        var action = new Action.MovePiece(MovablePiece.GARDENER, c);
        applier.apply(undoStack, action);

        assertEquals(c, board.getPieceCoord(MovablePiece.GARDENER));

        applier.apply(undoStack, Action.END_SIMULATION);

        assertEquals(new Coord(0, 0), board.getPieceCoord(MovablePiece.GARDENER));

        assertNoSevereLog();
    }

    @Test
    void drawObjective() {
        var action = new Action.TakeBambooSizeObjective();

        applier.apply(undoStack, action);

        assertEquals(1, player.getPrivateInventory().getObjectives().size());
        assertTrue(
                player.getPrivateInventory().getObjectives().get(0) instanceof BambooSizeObjective);

        applier.apply(undoStack, Action.END_SIMULATION);

        assertEquals(0, player.getPrivateInventory().getObjectives().size());

        assertNoSevereLog();
    }

    @Test
    void undoStackClearedOnEndTurn() {
        applier.apply(undoStack, Action.NONE);
        assertEquals(2, undoStack.size());
        applier.apply(undoStack, Action.END_TURN);
        assertEquals(0, undoStack.size());
    }
}
