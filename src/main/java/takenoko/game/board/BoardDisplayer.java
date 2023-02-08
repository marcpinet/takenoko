package takenoko.game.board;

import java.util.Optional;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.Tile;

public class BoardDisplayer {
    private final Board board;

    public BoardDisplayer(Board board) {
        this.board = board;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = -6; i < 6; i++) {
            for (int j = -6; j < 6; j++) {
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
        for (var piece : MovablePiece.values())
            sb.append(piece.toString())
                    .append(": ")
                    .append(board.getPieceCoord(piece))
                    .append("\n");
        return sb.toString();
    }

    private Optional<Tile> getTileAtDisplayPosition(int i, int j) {
        for (var c : board.getPlacedCoords()) {

            var row = c.x();
            var col = c.y() + (c.x() + (c.x() & 1)) / 2;
            if (row == i && col == j) {
                try {
                    return Optional.of(board.getTile(c));
                } catch (BoardException e) {
                    throw new IllegalStateException();
                }
            }
        }
        return Optional.empty();
    }
}
