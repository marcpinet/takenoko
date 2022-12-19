package takenoko;

public interface Tile {
    boolean isCultivable();

    boolean isSideIrrigated(TileSide side);

    void irrigateSide(TileSide side) throws IrrigationException;
}
