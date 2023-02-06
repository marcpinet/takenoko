package takenoko.game.tile;

import java.util.EnumMap;
import java.util.Map;

public class PowerUpReserve {
    private final EnumMap<PowerUp, Integer> reserve;

    public PowerUpReserve(Map<PowerUp, Integer> reserve) {
        this.reserve = new EnumMap<>(reserve);
    }

    public PowerUpReserve() {
        this(
                Map.of(
                        PowerUp.NONE, 0,
                        PowerUp.WATERSHED, 3,
                        PowerUp.ENCLOSURE, 3,
                        PowerUp.FERTILIZER, 3));
    }

    public void pick(PowerUp powerUp) throws PowerUpNotAvailableException {
        if (reserve.get(powerUp) <= 0) {
            throw new PowerUpNotAvailableException();
        }
        reserve.put(powerUp, reserve.get(powerUp) - 1);
    }

    public boolean canPick(PowerUp powerUp) {
        return reserve.get(powerUp) > 0;
    }

    public void putBack(PowerUp powerUp) {
        reserve.put(powerUp, reserve.get(powerUp) + 1);
    }
}
