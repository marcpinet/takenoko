package takenoko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.bot.EasyBot;
import takenoko.objective.Objective;
import takenoko.objective.TilePatternObjective;

public class GameTest {
    Game game;
    List<Player> players;
    List<Objective> objectives;

    @BeforeEach
    public void setUp() {
        players = List.of(new EasyBot(new Random(0)), new EasyBot(new Random(0)));
        objectives =
                List.of(
                        new TilePatternObjective(TilePatternObjective.TRIANGLE_2x2),
                        new TilePatternObjective(TilePatternObjective.SQUARE_2x2),
                        new TilePatternObjective(TilePatternObjective.LINE_3),
                        new TilePatternObjective(TilePatternObjective.LINE_2));
        game = new Game(players, objectives, Logger.getGlobal());
    }

    @Test
    void testGame() {
        // For now, just make sure the game doesn't crash
        assertEquals(players.get(0), game.play());
    }
}
