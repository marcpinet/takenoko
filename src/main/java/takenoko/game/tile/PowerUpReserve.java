package takenoko.game.tile;

import java.util.HashMap;
import java.util.Map;

public class PowerUpReserve {
    private final HashMap<PowerUp, Integer> reserve;

    public PowerUpReserve(HashMap<PowerUp, Integer> reserve) {
        this.reserve = reserve;
    }

    public PowerUpReserve() {
        this(
                new HashMap<>(
                        Map.of(
                                PowerUp.NONE, 0,
                                PowerUp.WATERSHED, 3,
                                PowerUp.ENCLOSURE, 3,
                                PowerUp.FERTILIZER, 3)));
    }

    public boolean pick(PowerUp powerUp) {
        if (reserve.get(powerUp) > 0) {
            reserve.put(powerUp, reserve.get(powerUp) - 1);
            return true;
        }
        return false;
    }
}
