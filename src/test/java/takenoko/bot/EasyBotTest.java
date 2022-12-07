package takenoko.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import takenoko.*;
import takenoko.utils.Pair;

class EasyBotTest {

    @Test
    void testChooseActions() {
        Board board = new Board();
        EasyBot bot = new EasyBot();

        Pair<Action, Action> result = bot.chooseActions(board);

        // Check both actions are not Action.NONE. If it's the case, then it means the stack of
        // tiles is empty
        assertTrue(result.first() instanceof Action.PlaceTile);
        assertTrue(result.second() instanceof Action.PlaceTile);

        // Check for coordinates validity
        assertTrue(
                board.getAvailableCoords().contains(((Action.PlaceTile) result.first()).coord()));
        assertTrue(
                board.getAvailableCoords().contains(((Action.PlaceTile) result.second()).coord()));

        // Check that both actions are not on the same coordinate
        assertNotEquals(
                ((Action.PlaceTile) result.first()).coord(),
                ((Action.PlaceTile) result.second()).coord());
    }
}
