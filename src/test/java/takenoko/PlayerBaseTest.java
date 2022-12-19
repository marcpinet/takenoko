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
    void testCredits() {
        player.beginTurn(3);
        assertEquals(3, player.availableActionCredits());

        player.chooseAction(board);
        assertEquals(2, player.availableActionCredits());

        player.chooseAction(board);
        assertEquals(1, player.availableActionCredits());

        player.chooseAction(board);
        assertEquals(0, player.availableActionCredits());

        // No more credits
        assertThrows(IllegalStateException.class, () -> player.chooseAction(board));
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
        public Action chooseActionImpl(Board board) {
            return Action.NONE;
        }
    }
}
