package takenoko;

public class PondTile implements Tile {
    public boolean isCultivable;

    public PondTile() {
        this.isCultivable = false;
    }

    @Override
    public boolean isCultivable() {
        return this.isCultivable;
    }
}
