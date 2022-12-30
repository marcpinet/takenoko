package takenoko.game.tile;

import java.util.HashMap;
import java.util.Map;

public class BambooTile implements Tile {
    private int bambooSize;

    private final Map<TileSide, Boolean> irrigatedSides;

    private PowerUp powerUp;

    private final Color color;

    public BambooTile(Color color) {
        bambooSize = 0;
        this.powerUp = PowerUp.NONE;
        irrigatedSides = new HashMap<>();
        for (TileSide side : TileSide.values()) {
            irrigatedSides.put(side, false);
        }
        this.color = color;
    }

    public BambooTile(Color color, PowerUp powerUp) {
        this(color);
        this.powerUp = powerUp;
    }

    public void irrigateSide(TileSide side) {
        irrigatedSides.put(side, true);
    }

    public boolean isIrrigated() {
        return irrigatedSides.values().stream().anyMatch(Boolean::booleanValue)
                || powerUp == PowerUp.WATERSHED;
    }

    public boolean isSideIrrigable(TileSide side) {
        if (isSideIrrigated(side)) {
            return false;
        }
        return irrigatedSides.get(side.leftSide()) || irrigatedSides.get(side.rightSide());
    }

    @Override
    public boolean isSideIrrigated(TileSide side) {
        return irrigatedSides.get(side);
    }

    public void growBamboo() throws BambooSizeException, BambooIrrigationException {
        if (!isCultivable()) {
            throw new BambooSizeException("Max bamboo size reached.");
        }
        if (!isIrrigated()) {
            throw new BambooIrrigationException("Cannot grow bamboo on an unirrigated tile");
        }
        bambooSize++;

        if (powerUp == PowerUp.FERTILIZER && isCultivable()) {
            bambooSize++;
        }
    }

    public int getBambooSize() {
        return bambooSize;
    }

    public void shrinkBamboo() throws BambooSizeException {
        if (bambooSize > 0) {
            bambooSize--;
        } else {
            throw new BambooSizeException("Error: Bamboo size cannot be negative.");
        }
    }

    @Override
    public boolean isCultivable() {
        return bambooSize < 4;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public Color getColor() {
        return color;
    }

    public void setPowerUp(PowerUp powerUp) throws PowerUpException {
        if (this.powerUp != PowerUp.NONE) {
            throw new PowerUpException("Error: Tile already has a power up.");
        }
        this.powerUp = powerUp;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BambooTile bambooTile && bambooTile.bambooSize == bambooSize;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
