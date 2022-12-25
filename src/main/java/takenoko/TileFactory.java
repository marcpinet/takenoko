package takenoko;

public class TileFactory {
    public TileFactory() {
        // will be used later
    }

    public Tile randomTile() {
        return new BambooTile();
    }
}
