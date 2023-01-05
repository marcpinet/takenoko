package takenoko.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import takenoko.utils.Coord;
import utils.TestLogHandler;

public class GameTest {
    Game game;
    List<Player> players;
    List<Objective> objectives;
    @Mock EasyBot p1 = mock(EasyBot.class);
    @Mock EasyBot p2 = mock(EasyBot.class);
    @Mock TilePatternObjective line2 = mock(TilePatternObjective.class);
    TileDeck tileDeck;

    Logger logger;
    TestLogHandler logHandler;

    @BeforeEach
    public void setUp() throws InventoryException {
        players = List.of(p1, p2);
        objectives = List.of(line2);
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
    void testGame() throws PlayerException, InventoryException {
        // For the moment, we verify only one completed objective, because the game stop as soon as
        // an objective is complete.

        Action.PlaceTile firstTile =
                new Action.PlaceTile(new Coord(0, 1), TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
        Action.PlaceTile secondTile =
                new Action.PlaceTile(new Coord(0, 2), TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
        Action.PlaceTile thirdTile =
                new Action.PlaceTile(new Coord(1, 0), TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
        Action.PlaceTile fourthTile =
                new Action.PlaceTile(new Coord(-1, +1), TileDeck.DEFAULT_DRAW_TILE_PREDICATE);

        // Don't forget that unveil an objective is an action, just like place a tile
        when(p1.chooseAction(any(), any()))
                .thenReturn(firstTile, secondTile, new Action.UnveilObjective(line2));
        // If we don't put the last "false", we will be trapped in an infinite loop because players
        // will immediately end the turn before playing it.
        when(p1.wantsToEndTurn()).thenReturn(false, false, true, false);
        when(p2.chooseAction(any(), any())).thenReturn(thirdTile, fourthTile);
        when(p2.wantsToEndTurn()).thenReturn(false, false, true, false);
        // line2 objective is achieved after firstTile action is done.
        when(line2.isAchieved(any(), eq(firstTile))).thenReturn(false);
        when(line2.isAchieved(any(), eq(secondTile))).thenReturn(true);
        when(line2.wasAchievedAfterLastCheck()).thenReturn(true);

        var inventory1 = new Inventory();
        var inventory2 = new Inventory();
        when(p1.getInventory()).thenReturn(inventory1);
        when(p2.getInventory()).thenReturn(inventory2);

        game = new Game(players, objectives, Logger.getGlobal(), tileDeck);

        try {
            assertEquals(players.get(0), game.play());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e);
        }
        assertNoSevereLog();
    }

    @Test
    void randomGame() throws InventoryException {
        // We just want to check that the game is not crashing for a given seed
        // Carefully picked seed so that the game is not infinite

        final int seed1 = 809349372;
        final int seed2 = 143379137;
        List<Player> players =
                List.of(new EasyBot(new Random(seed1)), new EasyBot(new Random(seed2)));
        List<Objective> objectives =
                List.of(new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3));
        var game = new Game(players, objectives, logger, new TileDeck(new Random(0)));

        assertDoesNotThrow(game::play);
        assertNoSevereLog();
    }
}
