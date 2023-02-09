package takenoko.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.Objective;
import takenoko.game.tile.TileDeck;
import takenoko.player.Player;
import takenoko.player.PrivateInventory;
import takenoko.player.bot.RandomBot;
import takenoko.player.bot.SaboteurBot;
import utils.TestLogHandler;

class GameTest {
    TileDeck tileDeck;
    TestLogHandler logHandler;
    Logger logger;

    @BeforeEach
    public void setUp() {
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
    void getWinner() {
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
        var game =
                new Game(players, logger, tileDeck, new WeatherDice(new Random(0)), new Random(0));

        assertEquals(Optional.empty(), game.play());
        assertNoSevereLog();
    }

    @Test
    void initialDrawAtStart() {
        var p1 = new RandomBot(new Random(0), "edgar");
        var p2 = new RandomBot(new Random(0), "marc");

        new Game(List.of(p1, p2), logger, tileDeck, new WeatherDice(new Random(0)), new Random(0));
        assertEquals(3, p1.getPrivateInventory().getObjectives().size());
    }

    @Test
    void randomGame() {
        // We just want to check that the game is not crashing
        // So we run some games

        for (int i = 0; i < 10; i++) {
            List<Player> players =
                    List.of(
                            new RandomBot(new Random(), "marc"),
                            new SaboteurBot(new Random(), "edgar"));
            var deck = new TileDeck(new Random());
            var game = new Game(players, logger, deck, new WeatherDice(new Random()), new Random());
            game.play();
            assertNoSevereLog();
        }
    }

    @Test
    void endOfGame() {
        Random r = new Random(0);
        Player p1 = mock(Player.class);
        Player p2 = new RandomBot(new Random(), "edgar");
        VisibleInventory vi = mock(VisibleInventory.class);
        @SuppressWarnings("unchecked")
        List<Objective> li = mock(ArrayList.class);
        when(p1.getVisibleInventory()).thenReturn(vi);
        when(p1.getPrivateInventory()).thenReturn(new PrivateInventory());
        when(vi.getFinishedObjectives()).thenReturn(li);
        when(li.size()).thenReturn(9);

        var players = List.of(p1, p2);
        var game = new Game(players, logger, tileDeck, new WeatherDice(new Random(0)), r);
        assertTrue(game.endOfGame());

        Player p3 = new RandomBot(new Random(), "marc");
        players = List.of(p1, p2, p3);
        game = new Game(players, logger, tileDeck, new WeatherDice(new Random(0)), r);
        assertFalse(game.endOfGame());
        when(li.size()).thenReturn(8);
        assertTrue(game.endOfGame());

        Player p4 = new RandomBot(new Random(), "cléclé");
        players = List.of(p1, p2, p3, p4);
        game = new Game(players, logger, tileDeck, new WeatherDice(new Random(0)), r);
        assertFalse(game.endOfGame());
        when(li.size()).thenReturn(7);
        assertTrue(game.endOfGame());
    }

    @Test
    void weatherSun() {
        Random r = new Random(0);
        Player p1 = spy(new RandomBot(new Random(0), "loic2"));
        Player p2 = new RandomBot(new Random(0), "loic1");

        var players = List.of(p1, p2);

        var dice = mock(WeatherDice.class);
        when(dice.throwDice()).thenReturn(WeatherDice.Face.SUN);

        var game = new Game(players, logger, tileDeck, dice, r);
        game.play(1);

        verify(dice, times(2)).throwDice();
        verify(p1).beginTurn(intThat(i -> i == 3));

        assertNoSevereLog();
    }
}
