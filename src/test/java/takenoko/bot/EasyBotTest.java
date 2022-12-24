package takenoko.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.*;
import takenoko.objective.Objective;

class EasyBotTest {
    Random randomSource;

    @BeforeEach
    void setUp() {
        // Fixed seed
        randomSource = new Random(0);
    }

    @Test
    void testChooseActions() throws PlayerException {
        Board board = new Board();
        EasyBot bot = new EasyBot(randomSource);

        bot.beginTurn(1);
        Action action = bot.chooseAction(board);

        assertTrue(action instanceof Action.PlaceTile);

        var expectedAction = new Action.PlaceTile(new Coord(-1, 0), new BambooTile());
        assertEquals(expectedAction, action);
    }

    @Test
    void unveilsObjectiveASAP() throws PlayerException {
        Board board = new Board();
        EasyBot bot = new EasyBot(randomSource);

        var objMock = mock(Objective.class);
        when(objMock.wasAchievedAfterLastCheck()).thenReturn(true);

        bot.addObjective(objMock);

        bot.beginTurn(1);
        Action action = bot.chooseAction(board);

        assertEquals(new Action.UnveilObjective(objMock), action);
    }
}
