package takenoko;

public class BambooTile implements Tile {
    @Override
    public boolean isCultivable() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BambooTile;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
