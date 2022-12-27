package takenoko;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import utils.Pair;

public class Board {
    public static final Coord POND_COORD = new Coord(0, 0);
    private final Map<Coord, Tile> tiles;
    private Pair<MovablePiece, Coord> gardener = Pair.of(MovablePiece.GARDENER, POND_COORD);

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
        var c = tiles.get(coord);
        if (c == null)
            throw new IrrigationException("Error: There is no tile at these coordinates.");
        c.irrigateSide(side);

        var c2 = tiles.get(coord.adjacentCoordSide(side));
        if (c2 != null) c2.irrigateSide(side.oppositeSide());
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

    public Set<Coord> getPlacedCoords() {
        return tiles.keySet();
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

    public void move(MovablePiece pieceType, Coord coord)
            throws BoardException, BambooSizeException, BambooIrrigationException {
        if (!tiles.containsKey(coord)) {
            throw new BoardException(
                    "Error: the tile with these coordinates is not present on the board.");
        }

        // Checking if coords are in a straight line
        Coord pieceCoord = gardener.second(); // For now, until we implement the panda
        if (!pieceCoord.isAlignedWith(coord)) {
            throw new BoardException("Error: this piece can only move in a straight line.");
        }

        // Updating piece position
        if (pieceType == MovablePiece.GARDENER) {
            gardener = Pair.of(pieceType, coord);
            // Making bamboo on the tile grow
            Tile tile = tiles.get(coord);
            if (tile instanceof BambooTile bambooTile) bambooTile.growBamboo();
        }
        // TODO: implement panda
    }
}
