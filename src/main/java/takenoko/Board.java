package takenoko;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    public void placeTile(Coord c, Tile t) throws BoardException, IrrigationException {
        if (tiles.containsKey(c)) {
            throw new BoardException(
                    "Error: There is already a tile present at theses coordinates.");
        }
        tiles.put(c, t);

        for (TileSide side : TileSide.values()) {
            Coord adjacentCoord = c.adjacentCoordSide(side);
            if (tiles.containsKey(adjacentCoord)) {
                if (tiles.get(adjacentCoord).isSideIrrigated(side.oppositeSide())) {
                    t.irrigateSide(side);
                }
            }
        }
    }

    public void placeIrrigation(Coord coord, TileSide side) throws IrrigationException {
        try {
            tiles.get(coord).irrigateSide(side);
            tiles.get(coord.adjacentCoordSide(side)).irrigateSide(side.oppositeSide());
        } catch (IrrigationException e) {
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                var tile = getTileAtDisplayPosition(i, j);
                if (tile.isPresent()) {
                    sb.append("(").append(i).append(",").append(j).append(") ");
                } else {
                    sb.append("----- ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private Optional<Tile> getTileAtDisplayPosition(int i, int j) {
        for (var keyval : tiles.entrySet()) {
            Coord c = keyval.getKey();
            var row = c.x();
            var col = c.y() + (c.x() + (c.x() & 1)) / 2;
            if (row == i && col == j) {
                return Optional.of(keyval.getValue());
            }
        }
        return Optional.empty();
    }

    public boolean contains(Coord coord) {
        return tiles.containsKey(coord);
    }
}
