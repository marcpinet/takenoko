package takenoko;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Coord, Tile> tiles;

    public Board() {
        tiles = new HashMap<>();
        tiles.put(new Coord(0, 0), new PondTile());
    }

    public void placeTile(Coord c, Tile t) {
        tiles.put(c, t);
    }

    public Tile getTile(Coord c) {
        return tiles.get(c);
    }
}
