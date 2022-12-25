package takenoko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerBaseTest {

    Player player;
    Board board;

    @BeforeEach
    void setUp() {
        player = new TestPlayer();
        board = new Board();
    }

    @Test
    void testCredits() throws PlayerException {
        player.beginTurn(3);
        assertEquals(3, player.availableActionCredits());

        var validator = new ActionValidator(board, 20);

        var action = player.chooseAction(board, validator);
        player.commitAction(action);
        assertEquals(2, player.availableActionCredits());

        action = player.chooseAction(board, validator);
        player.commitAction(action);
        assertEquals(1, player.availableActionCredits());

        action = player.chooseAction(board, validator);
        player.commitAction(action);
        assertEquals(0, player.availableActionCredits());

        // No more credits
        var finalAction = player.chooseAction(board, validator);
        assertThrows(IllegalStateException.class, () -> player.commitAction(finalAction));
    }

    @Test
    void testTakeIrrigationSticks() {
        player.beginTurn(3);
        player.takeIrrigationStick();
        assertEquals(1, player.getInventory());
    }

    private static class TestPlayer extends PlayerBase<TestPlayer>
            implements PlayerBase.PlayerBaseInterface {
        @Override
        public Action chooseActionImpl(Board board, ActionValidator validator) {
            return Action.NONE;
        }
    }
}
