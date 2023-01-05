package takenoko.player.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.objective.Objective;
import takenoko.player.InventoryException;
import takenoko.player.PlayerException;
import takenoko.utils.Coord;

class EasyBotTest {
    Random randomSource;
    @Mock ActionValidator validator = mock(ActionValidator.class);

    @BeforeEach
    void setUp() {
        // Fixed seed
        randomSource = new Random(0);
        when(validator.isValid(any())).thenReturn(true);
    }

    @Test
    void testChooseActions() throws PlayerException {
        Board board = new Board();
        EasyBot bot = new EasyBot(randomSource);

        bot.beginTurn(1);
        Action action = bot.chooseAction(board, validator);

        assertTrue(action instanceof Action.PlaceTile);

        var expected = new Coord(-1, 0);
        assertEquals(expected, ((Action.PlaceTile) action).coord());
    }

    @Test
    void unveilsObjectiveASAP() throws PlayerException, InventoryException {
        Board board = new Board();
        EasyBot bot = new EasyBot(randomSource);

        var objMock = mock(Objective.class);
        when(objMock.wasAchievedAfterLastCheck()).thenReturn(true);

        bot.getInventory().addObjective(objMock);

        bot.beginTurn(1);
        Action action = bot.chooseAction(board, validator);

        assertEquals(new Action.UnveilObjective(objMock), action);
    }
}
