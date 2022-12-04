package takenoko;

import java.util.HashMap;
import java.util.Map;

public class TileBoard {
    private Map<Coord, Tile> tiles;

    public TileBoard() {
        tiles = new HashMap<>();
        tiles.put(new Coord(0, 0), new PondTile());
    }

    public void placeTile(Coord c, Tile t) {
        tiles.put(c, t);
    }

    public Map<Coord, Tile> getTiles() {
        return tiles;
    }
}
