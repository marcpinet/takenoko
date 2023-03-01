package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PowerUpReserveTest {
    PowerUpReserve reserve;

    @BeforeEach
    void setUp() {
        reserve =
                new PowerUpReserve(
                        Map.of(
                                PowerUp.NONE, 0,
                                PowerUp.WATERSHED, 1,
                                PowerUp.ENCLOSURE, 1,
                                PowerUp.FERTILIZER, 1));
    }

    @Test
    void pick() throws PowerUpNotAvailableException {
        assertFalse(reserve.canPick(PowerUp.NONE));
        assertThrows(PowerUpNotAvailableException.class, () -> reserve.pick(PowerUp.NONE));

        for (var powerUp : PowerUp.values()) {
            if (powerUp != PowerUp.NONE) {
                assertTrue(reserve.canPick(powerUp));
                // No more power up of this type
                reserve.pick(powerUp);
                assertFalse(reserve.canPick(powerUp));
                assertThrows(PowerUpNotAvailableException.class, () -> reserve.pick(powerUp));
            }
        }
    }
}
