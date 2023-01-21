package takenoko.game.board;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import takenoko.game.objective.Objective;
import takenoko.game.tile.*;
import takenoko.player.Player;
import takenoko.utils.Coord;

public class Board {
    public static final Coord POND_COORD = new Coord(0, 0);
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
        Set<Coord> intersecWithTiles =
                Stream.of(c.adjacentCoords())
                        .filter(tiles::containsKey)
                        .collect(Collectors.toSet());

        if (intersecWithTiles.isEmpty()) return false;

        return intersecWithTiles.size() != 1 || intersecWithTiles.contains(POND_COORD);
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
                .filter(this::isAvailableCoord)
                .collect(Collectors.toSet());
    }

    public Set<Coord> getPlacedCoords() {
        return tiles.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                var tile = getTileAtDisplayPosition(i, j);
                if (tile.isPresent()) {
                    if (tile.get() instanceof BambooTile bambooTile) {
                        sb.append(' ')
                                .append(bambooTile.getColor().toString().charAt(0))
                                .append(' ');
                    } else {
                        sb.append("---");
                    }
                    sb.append("(").append(i).append(",").append(j).append(") ");

                } else {
                    sb.append("-------- ");
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

    // S1301: switch is actually more readable here
    @SuppressWarnings("java:S1301")
    public void move(MovablePiece pieceType, Coord coord, Player player) throws BoardException {
        if (!tiles.containsKey(coord)) {
            throw new BoardException(
                    "Error: the tile with these coordinates is not present on the board.");
        }

        Coord currentCoord = getPieceCoord(pieceType);
        if (!coord.isAlignedWith(currentCoord)) {
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

    public int getPlayerScore(Player p) {
        int score = 0;
        for (Objective o : p.getVisibleInventory().getFinishedObjectives()) {
            score += o.getScore();
        }
        return score;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
