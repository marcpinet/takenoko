package takenoko.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.tile.TileDeck;

class PlayerBaseTest {

    Player player;
    Board board;
    TileDeck deck;

    @BeforeEach
    void setUp() {
        player = new TestPlayer();
        board = new Board();
        deck = new TileDeck(new Random(0));
    }

    @Test
    void testCredits() throws PlayerException {
        player.beginTurn(3);
        assertEquals(3, player.availableActionCredits());

        player.getInventory().incrementIrrigation();
        var validator = new ActionValidator(board, deck, 20, player.getInventory());

        player.chooseAction(board, validator);
        assertEquals(2, player.availableActionCredits());

        player.chooseAction(board, validator);
        assertEquals(1, player.availableActionCredits());

        player.chooseAction(board, validator);
        assertEquals(0, player.availableActionCredits());

        // No more credits
        assertThrows(IllegalStateException.class, () -> player.chooseAction(board, validator));
    }

    private static class TestPlayer extends PlayerBase<TestPlayer>
            implements PlayerBase.PlayerBaseInterface {
        @Override
        public Action chooseActionImpl(Board board, ActionValidator validator) {
            return Action.NONE;
        }
    }
}
