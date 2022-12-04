package takenoko;

public class PondTile implements Tile {
    private boolean isCultivable;

    public PondTile() {
        this.isCultivable = false;
    }

    @Override
    public boolean isCultivable() {
        return this.isCultivable;
    }
}
