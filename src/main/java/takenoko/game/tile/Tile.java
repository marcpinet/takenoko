package takenoko.game.tile;

public interface Tile {
    boolean isCultivable();

    boolean isSideIrrigated(TileSide side);

    void irrigateSide(TileSide side) throws IrrigationException;

    void removeIrrigation(TileSide side) throws IrrigationException;
}
