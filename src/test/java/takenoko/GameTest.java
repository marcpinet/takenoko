package takenoko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.bot.DefaultBot;

public class GameTest {
    Game game;
    List<Player> players;

    @BeforeEach
    public void setUp() {
        players = List.of(new DefaultBot(), new DefaultBot());
        game = new Game(players, Logger.getGlobal());
    }

    @Test
    void testGame() {
        // For now, just make sure the game doesn't crash
        assertEquals(players.get(0), game.play());
    }
}
