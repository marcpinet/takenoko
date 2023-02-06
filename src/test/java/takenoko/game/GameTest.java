package takenoko.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.Objective;
import takenoko.game.tile.TileDeck;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PlayerException;
import takenoko.player.PrivateInventory;
import takenoko.player.bot.EasyBot;
import utils.TestLogHandler;

class GameTest {
    TileDeck tileDeck;
    TestLogHandler logHandler;
    Logger logger;

    @BeforeEach
    public void setUp() throws InventoryException {
        tileDeck = new TileDeck(new Random(0));

        logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        logHandler = new TestLogHandler();
        logger.addHandler(logHandler);
    }

    void assertNoSevereLog() {
        assertEquals(
                Collections.emptyList(),
                logHandler.getRecords().stream()
                        .filter(r -> r.getLevel().equals(java.util.logging.Level.SEVERE))
                        // so that we can see the messages
                        .map(LogRecord::getMessage)
                        .toList());
    }

    @Test
    void testGetWinner() throws PlayerException {
        var p1 = mock(Player.class);
        when(p1.getVisibleInventory()).thenReturn(new VisibleInventory());
        when(p1.getPrivateInventory()).thenReturn(new PrivateInventory());
        when(p1.getScore()).thenReturn(1);
        when(p1.chooseAction(any(), any())).thenReturn(Action.END_TURN);

        var p2 = mock(Player.class);
        when(p2.getVisibleInventory()).thenReturn(new VisibleInventory());
        when(p2.getPrivateInventory()).thenReturn(new PrivateInventory());
        when(p2.getScore()).thenReturn(2);
        when(p2.chooseAction(any(), any())).thenReturn(Action.END_TURN);

        var players = List.of(p1, p2);
        var game = new Game(players, logger, tileDeck, new Random(0));

        assertEquals(Optional.of(p2), game.play());
        assertNoSevereLog();
    }

    @Test
    void testInitialDrawAtStart() {
        var p1 = new EasyBot(new Random(0));
        var p2 = new EasyBot(new Random(0));

        var game = new Game(List.of(p1, p2), logger, tileDeck, new Random(0));

        assertEquals(3, p1.getPrivateInventory().getObjectives().size());
    }

    @Test
    void randomGame() {
        // We just want to check that the game is not crashing
        // So we run some games

        for (int i = 0; i < 10; i++) {
            List<Player> players = List.of(new EasyBot(new Random()), new EasyBot(new Random()));
            var game = new Game(players, logger, tileDeck, new Random());
            game.play();
            assertNoSevereLog();
        }
    }

    @Test
    void testEndOfGame() {

        Player p1 = mock(Player.class);
        VisibleInventory vi = mock(VisibleInventory.class);
        List<Objective> li = mock(ArrayList.class);
        when(p1.getVisibleInventory()).thenReturn(vi);
        when(vi.getFinishedObjectives()).thenReturn(li);
        when(li.size()).thenReturn(9);
        Player p2 = new EasyBot(new Random());
        var players = List.of(p1, p2);
        var game = new Game(players, logger, tileDeck);
        assertTrue(game.endOfGame());

        Player p3 = new EasyBot(new Random());
        players = List.of(p1, p2, p3);
        game = new Game(players, logger, tileDeck);
        when(li.size()).thenReturn(8);
        assertTrue(game.endOfGame());

        Player p4 = new EasyBot(new Random());
        players = List.of(p1, p2, p3, p4);
        game = new Game(players, logger, tileDeck);
        when(li.size()).thenReturn(7);
        assertTrue(game.endOfGame());
    }
}
