package takenoko.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.action.PossibleActionLister;
import takenoko.game.GameInventory;
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

        player.getVisibleInventory().incrementIrrigation();
        var validator =
                new ActionValidator(
                        board,
                        new GameInventory(20, deck, new Random(0)),
                        player.getPrivateInventory(),
                        player.getVisibleInventory());
        var lister = new PossibleActionLister(board, validator, player.getPrivateInventory());

        player.chooseAction(board, lister);
        assertEquals(2, player.availableActionCredits());

        player.chooseAction(board, lister);
        assertEquals(1, player.availableActionCredits());

        player.chooseAction(board, lister);
        assertEquals(0, player.availableActionCredits());

        // No more credits
        assertThrows(IllegalStateException.class, () -> player.chooseAction(board, lister));
    }

    private static class TestPlayer extends PlayerBase<TestPlayer>
            implements PlayerBase.PlayerBaseInterface {
        @Override
        public Action chooseActionImpl(Board board, PossibleActionLister actionLister) {
            return Action.NONE;
        }
    }
}
