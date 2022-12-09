package takenoko.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.*;

class EasyBotTest {
    Random randomSource;

    @BeforeEach
    void setUp() {
        // Fixed seed
        randomSource = new Random(0);
    }

    @Test
    void testChooseActions() {
        Board board = new Board();
        EasyBot bot = new EasyBot(randomSource);

        bot.beginTurn(1);
        Action action = bot.chooseAction(board);

        assertTrue(action instanceof Action.PlaceTile);

        var expectedAction = new Action.PlaceTile(new Coord(0, -1), new BambooTile());
        assertEquals(expectedAction, action);
    }
}
