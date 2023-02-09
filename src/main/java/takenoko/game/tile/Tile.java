package takenoko.game.tile;

public interface Tile {
    boolean isSideIrrigated(TileSide side);

    void irrigateSide(TileSide side) throws IrrigationException;
}
