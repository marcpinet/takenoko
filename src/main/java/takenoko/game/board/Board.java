package takenoko.game.board;

import java.util.*;
import java.util.stream.Stream;
import takenoko.game.tile.*;
import takenoko.player.Player;
import takenoko.utils.Coord;

public class Board {
    public static final Coord POND_COORD = new Coord(0, 0);
    private static final String TILE_EXCEPTION_MESSAGE =
            "Error: the tile with these coordinates is not present on the board.";
    private final Map<Coord, Tile> tiles;
    private final Map<MovablePiece, Coord> movablePieces;
    private final List<Player> players;

    public Board() {
        this(Collections.emptyList());
    }

    public Board(List<Player> players) {
        tiles = new HashMap<>();
        tiles.put(POND_COORD, new PondTile());

        movablePieces = new EnumMap<>(MovablePiece.class);
        movablePieces.put(MovablePiece.GARDENER, POND_COORD);
        movablePieces.put(MovablePiece.PANDA, POND_COORD);
        this.players = players;
    }

    public Board(Board other) {
        this(other.players);
        restore(other);
    }

    public void restore(Board other) {
        for (var entry : other.tiles.entrySet()) {
            if (entry.getValue() instanceof BambooTile tile) {
                tiles.put(entry.getKey(), new BambooTile(tile));
            }
        }

        movablePieces.putAll(other.movablePieces);
    }

    public void placeTile(Coord c, Tile t) throws BoardException, IrrigationException {
        if (!isAvailableCoord(c)) {
            throw new BoardException("Coord " + c + " is not available");
        }
        tiles.put(c, t);

        for (TileSide side : TileSide.values()) {
            Coord adjacentCoord = c.adjacentCoordSide(side);
            if (tiles.containsKey(adjacentCoord)
                    && tiles.get(adjacentCoord).isSideIrrigated(side.oppositeSide())) {
                t.irrigateSide(side);
            }
        }
    }

    public boolean isAvailableCoord(Coord c) {
        if (tiles.containsKey(c)) {
            return false;
        }

        // Check if the tile is adjacent to the pond AND/OR to at least 2 tiles already on the board
        // (including the pond)
        int adjacentTiles = 0;
        for (var adjacentCoord : c.adjacentCoords()) {
            if (adjacentCoord.equals(POND_COORD)) {
                return true;
            }

            if (tiles.containsKey(adjacentCoord)) {
                adjacentTiles++;
            }
            if (adjacentTiles >= 2) {
                return true;
            }
        }
        return false;
    }

    public void placeIrrigation(Coord coord, TileSide side)
            throws IrrigationException, BoardException {
        var c = tiles.get(coord);
        if (c == null) throw new BoardException(TILE_EXCEPTION_MESSAGE);
        c.irrigateSide(side);

        var c2 = tiles.get(coord.adjacentCoordSide(side));
        if (c2 != null) c2.irrigateSide(side.oppositeSide());
    }

    public Tile getTile(Coord c) throws BoardException {
        if (!tiles.containsKey(c)) {
            throw new BoardException(TILE_EXCEPTION_MESSAGE);
        }
        return tiles.get(c);
    }

    public void removeTile(Coord coord) throws BoardException {
        if (tiles.remove(coord) == null) {
            throw new BoardException(TILE_EXCEPTION_MESSAGE);
        }
    }

    public Set<Coord> getAvailableCoords() {
        Set<Coord> availableCoords = new HashSet<>();
        for (Coord c : tiles.keySet()) {
            for (Coord adjacentCoord : c.adjacentCoords()) {
                if (isAvailableCoord(adjacentCoord)) {
                    availableCoords.add(adjacentCoord);
                }
            }
        }
        return availableCoords;
    }

    public Set<Coord> getPlacedCoords() {
        return tiles.keySet();
    }

    public boolean contains(Coord coord) {
        return tiles.containsKey(coord);
    }

    // S1301: switch is actually more readable here
    @SuppressWarnings("java:S1301")
    public void move(
            MovablePiece pieceType, Coord coord, Player player, boolean overrideStraightLineLimit)
            throws BoardException {
        if (!tiles.containsKey(coord)) {
            throw new BoardException(TILE_EXCEPTION_MESSAGE);
        }

        Coord currentCoord = getPieceCoord(pieceType);
        if (!overrideStraightLineLimit && !coord.isAlignedWith(currentCoord)) {
            throw new BoardException(
                    "Error: the " + pieceType + " can only move on a straight line.");
        }

        // Updating piece position
        movablePieces.put(pieceType, coord);
        // Apply their actions
        switch (pieceType) {
            case GARDENER -> growTilesWithGardener(coord);
            case PANDA -> eatBambooWithPanda(coord, player);
        }
    }

    public void move(MovablePiece pieceType, Coord coord, Player player) throws BoardException {
        move(pieceType, coord, player, false);
    }

    public Coord getPieceCoord(MovablePiece pieceType) {
        return movablePieces.get(pieceType);
    }

    private void growTilesWithGardener(Coord gardenerPos) {
        Tile tile = tiles.get(gardenerPos);
        // moving the gardener on the pond tile does nothing
        if (!(tile instanceof BambooTile center)) {
            return;
        }

        var adjacentTiles =
                Stream.concat(Stream.of(gardenerPos), Arrays.stream(gardenerPos.adjacentCoords()));

        var adjacentSameColorBamboos =
                adjacentTiles
                        .filter(tiles::containsKey)
                        .filter(c -> tiles.get(c) instanceof BambooTile)
                        .map(c -> (BambooTile) tiles.get(c))
                        .filter(b -> b.getColor() == center.getColor())
                        .filter(BambooTile::isIrrigated)
                        .filter(b -> b.getBambooSize() < 3)
                        .toList();

        for (BambooTile bambooTile : adjacentSameColorBamboos) {
            try {
                bambooTile.growBamboo();
            } catch (BambooSizeException | BambooIrrigationException e) {
                // This should never happen
                throw new IllegalStateException(e);
            }
        }
    }

    private void eatBambooWithPanda(Coord coord, Player player) {
        Tile tile = tiles.get(coord);
        if (tile instanceof BambooTile bambooTile && bambooTile.getBambooSize() > 0) {
            try {
                bambooTile.shrinkBamboo();
                player.getVisibleInventory().incrementBamboo(bambooTile.getColor());
            } catch (BambooSizeException e) {
                // This should never happen
                throw new IllegalStateException(e);
            }
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public String toString() {
        return new BoardDisplayer(this).toString();
    }
}
