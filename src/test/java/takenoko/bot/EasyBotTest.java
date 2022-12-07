package takenoko.bot;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import takenoko.Action;
import takenoko.Board;

class EasyBotTest {

    @Test
    void testChooseActions() {
        Board board = new Board();
        EasyBot bot = new EasyBot();

        Action action = bot.chooseAction(board);

        assertTrue(action instanceof Action.PlaceTile);

        assertTrue(board.getAvailableCoords().contains(((Action.PlaceTile) action).coord()));
    }
}
