package takenoko.bot;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import takenoko.Action;
import takenoko.Board;

class EasyBotTest {

    @Test
    void testChooseActions() {
        Board board = new Board();
        EasyBot bot = new EasyBot();

        Action action1 = bot.chooseActions(board);
        Action action2 = bot.chooseActions(board);

        // Check both actions are not Action.NONE. If it's the case, then it means the stack of
        // tiles is empty
        assertTrue(action1 instanceof Action.PlaceTile);
        assertTrue(action2 instanceof Action.PlaceTile);

        // Check for coordinates validity
        assertTrue(board.getAvailableCoords().contains(((Action.PlaceTile) action1).coord()));
        assertTrue(board.getAvailableCoords().contains(((Action.PlaceTile) action2).coord()));

        // Check that both actions are not on the same coordinate
        assertNotEquals(((Action.PlaceTile) action1).coord(), ((Action.PlaceTile) action2).coord());
    }
}
