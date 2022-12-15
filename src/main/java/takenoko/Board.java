package takenoko;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    public static final Coord POND_COORD = new Coord(0, 0);
    private final Map<Coord, Tile> tiles;

    public Board() {
        tiles = new HashMap<>();
        tiles.put(POND_COORD, new PondTile());
    }

    public void placeTile(Coord c, Tile t) throws BoardException {
        if (tiles.containsKey(c)) {
            throw new BoardException(
                    "Error: There is already a tile present at theses coordinates.");
        }
        tiles.put(c, t);
    }

    public Tile getTile(Coord c) throws BoardException {
        if (!tiles.containsKey(c)) {
            throw new BoardException(
                    "Error: the tile with these coordinates is not present on the board.");
        }
        return tiles.get(c);
    }

    public Set<Coord> getAvailableCoords() {
        return tiles.keySet().stream()
                .flatMap(c -> Stream.of(c.adjacentCoords()))
                .filter(c -> !tiles.containsKey(c))
                .collect(Collectors.toSet());
    }

    public void applyOnEachTile(Function<Tile, Void> f) {
        for (Map.Entry<Coord, Tile> entry : tiles.entrySet()) {
            f.apply(entry.getValue());
        }
    }
}
