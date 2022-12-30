package takenoko.game.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import takenoko.game.tile.*;
import takenoko.utils.Coord;
import takenoko.utils.Pair;

public class Board {
    public static final Coord POND_COORD = new Coord(0, 0);
    private final Map<Coord, Tile> tiles;
    private Pair<MovablePiece, Coord> gardener = Pair.of(MovablePiece.GARDENER, POND_COORD);
    private Pair<MovablePiece, Coord> panda = Pair.of(MovablePiece.PANDA, POND_COORD);

    public Board() {
        tiles = new HashMap<>();
        tiles.put(POND_COORD, new PondTile());
    }

    public void placeTile(Coord c, Tile t) throws BoardException, IrrigationException {
        if (tiles.containsKey(c)) {
            throw new BoardException(
                    "Error: There is already a tile present at theses coordinates.");
        }

        // Check if the tile is adjacent to the pond AND/OR to at least 2 tiles already on the board
        // (including the pond)
        Set<Coord> intersecWithTiles =
                Stream.of(c.adjacentCoords())
                        .filter(tiles::containsKey)
                        .collect(Collectors.toSet());

        if (intersecWithTiles.isEmpty())
            throw new BoardException(
                    "Error: The tile must be adjacent to at least one tile already on the board.");
        else if (intersecWithTiles.size() == 1 && !intersecWithTiles.contains(POND_COORD))
            throw new BoardException(
                    "Error: The tile is not adjacent to the pond or to at least 2 tiles already on"
                            + " the board.");

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
            throws BambooSizeException, BambooIrrigationException, BoardException {
        if (!tiles.containsKey(coord)) {
            throw new BoardException(
                    "Error: the tile with these coordinates is not present on the board.");
        }

        if (!coord.isAlignedWith(gardener.second())) {
            throw new BoardException("Error: the gardener can only move on an a straight line.");
        }

        // Updating piece position
        if (pieceType == MovablePiece.GARDENER) {
            gardener = Pair.of(pieceType, coord);
            // Making bamboo on the tile grow
            Tile tile = tiles.get(coord);
            if (tile instanceof BambooTile bambooTile) {
                bambooTile.growBamboo();
                // TODO : add adjacent bamboo tiles of the same color to grow
            }
        } else if (pieceType == MovablePiece.PANDA) {
            panda = Pair.of(pieceType, coord);
            // Making bamboo on the tile grow
            Tile tile = tiles.get(coord);
            if (tile instanceof BambooTile bambooTile) {
                bambooTile.shrinkBamboo();
            }
        }
    }

    public Coord getGardenerCoord() {
        return gardener.second();
    }

    public Coord getPandaCoord() {
        return panda.second();
    }
}
