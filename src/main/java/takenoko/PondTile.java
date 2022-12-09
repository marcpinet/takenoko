package takenoko;

public class PondTile implements Tile {
    @Override
    public boolean isCultivable() {
        return false;
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
