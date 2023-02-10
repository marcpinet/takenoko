package takenoko.game.tile;

/**
 * This interface define the concept of Tile in takenoko. Except for the pond tile, which is always
 * placed at (0, 0), tiles are only differentiated by color.
 *
 * <p>It isn't possible to place a tile if it's not adjacent to another tile already placed on the
 * board.
 *
 * <p>By default, all the PondTile's sides are irrigated, and you can't place a power up on it.
 */
public interface Tile {
    boolean isSideIrrigated(TileSide side);

    void irrigateSide(TileSide side) throws IrrigationException;
}
