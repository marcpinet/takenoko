package takenoko;

import java.util.HashMap;
import java.util.Map;

public class BambooTile implements Tile {
    private int bambooSize;

    private Map<TileSide, Boolean> irrigatedSides;

    public BambooTile() {
        bambooSize = 0;
        irrigatedSides = new HashMap<>();
        for (TileSide side : TileSide.values()) {
            irrigatedSides.put(side, false);
        }
    }

    public void irrigateSide(TileSide side) {
        irrigatedSides.put(side, true);
    }

    public boolean isIrrigated() {
        return irrigatedSides.values().stream().anyMatch(Boolean::booleanValue);
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

    public void growBamboo() throws BambooSizeException {
        if (!isCultivable()) {
            throw new BambooSizeException("Cannot grow bamboo on a pond");
        }
        if (!isIrrigated()) {
            throw new BambooSizeException("Cannot grow bamboo on an unirrigated tile");
        }
        bambooSize++;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof BambooTile bambooTile && bambooTile.bambooSize == bambooSize;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
