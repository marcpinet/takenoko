package takenoko.action;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActionTest {

    @Test
    void isSameTypeAs() {
        var a = Action.NONE;
        var b = Action.END_TURN;
        assertFalse(a.isSameTypeAs(b));
        assertTrue(a.isSameTypeAs(a));
    }
}
