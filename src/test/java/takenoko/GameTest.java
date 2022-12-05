package takenoko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {
    Game game;
    List<Player> players;

    @BeforeEach
    public void setUp() {
        players = List.of(new DefaultBot(), new DefaultBot());
        game = new Game(players, System.out);
    }

    @Test
    void testGame() {
        // For now, just make sure the game doesn't crash
        assertEquals(players.get(0), game.play());
    }
}
