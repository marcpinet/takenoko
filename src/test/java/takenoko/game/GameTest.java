package takenoko.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.game.objective.Objective;
import takenoko.game.objective.TilePatternObjective;
import takenoko.game.tile.Color;
import takenoko.game.tile.TileDeck;
import takenoko.player.Inventory;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PlayerException;
import takenoko.player.bot.EasyBot;
import utils.TestLogHandler;

class GameTest {
    TileDeck tileDeck;
    TestLogHandler logHandler;
    Logger logger;

    @BeforeEach
    public void setUp() throws InventoryException {
        tileDeck = new TileDeck(new Random(0));

        logger = Logger.getGlobal();
        logHandler = new TestLogHandler();
        logger.addHandler(logHandler);
    }

    void assertNoSevereLog() {
        assertFalse(
                logHandler.getRecords().stream()
                        .anyMatch(r -> r.getLevel().equals(java.util.logging.Level.SEVERE)));
    }

    @Test
    void testGetWinner() throws PlayerException {
        // Don't forget that unveil an objective is an action, just like place a tile
        var p1 = mock(Player.class);
        when(p1.getInventory()).thenReturn(new Inventory());
        when(p1.getScore()).thenReturn(1);
        when(p1.chooseAction(any(), any())).thenReturn(Action.END_TURN);

        var p2 = mock(Player.class);
        when(p2.getInventory()).thenReturn(new Inventory());
        when(p2.getScore()).thenReturn(2);
        when(p2.chooseAction(any(), any())).thenReturn(Action.END_TURN);

        var players = List.of(p1, p2);
        List<Objective> objectives =
                List.of(new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3));

        var game = new Game(players, objectives, logger, tileDeck);

        assertEquals(Optional.of(p2), game.play());
        assertNoSevereLog();
    }

    @Test
    void randomGame() {
        // We just want to check that the game is not crashing
        // So we run some games

        for (int i = 0; i < 10; i++) {
            List<Player> players = List.of(new EasyBot(new Random()), new EasyBot(new Random()));
            List<Objective> objectives =
                    List.of(new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3));

            var game = new Game(players, objectives, Logger.getGlobal(), tileDeck);
            game.play();
            assertNoSevereLog();
        }
    }
}
