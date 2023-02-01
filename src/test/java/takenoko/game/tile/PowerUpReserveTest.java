package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PowerUpReserveTest {
    PowerUpReserve reserve;

    @BeforeEach
    void setUp() {
        reserve =
                new PowerUpReserve(
                        new HashMap<>(
                                Map.of(
                                        PowerUp.NONE, 0,
                                        PowerUp.WATERSHED, 1,
                                        PowerUp.ENCLOSURE, 1,
                                        PowerUp.FERTILIZER, 1)));
    }

    @Test
    void pick() {
        assertFalse(reserve.pick(PowerUp.NONE));

        for (var powerUp : PowerUp.values()) {
            if (powerUp != PowerUp.NONE) {
                assertTrue(reserve.pick(powerUp));
                // No more power up of this type
                assertFalse(reserve.pick(powerUp));
            }
        }
    }
}
