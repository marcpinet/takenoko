package takenoko.game.tile;

public class PondTile implements Tile {
    public boolean isSideIrrigated(TileSide side) {
        return true;
    }

    public void irrigateSide(TileSide side) throws IrrigationException {
        throw new IrrigationException("Cannot irrigate a pond");
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PondTile;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
