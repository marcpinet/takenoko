package takenoko.game;

import takenoko.game.objective.BambooSizeObjective;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.ObjectiveDeck;
import takenoko.game.objective.TilePatternObjective;
import takenoko.game.tile.TileDeck;

public class GameInventory {
    int irrigationSticks;
    TileDeck tileDeck;
    private final ObjectiveDeck<TilePatternObjective> tilePatternObjectiveDeck;
    private final ObjectiveDeck<BambooSizeObjective> bambooSizeObjectiveDeck;
    private final ObjectiveDeck<HarvestingObjective> harvestingObjectiveDeck;

    public GameInventory(int irrigationSticks, TileDeck tileDeck) {
        this(
                irrigationSticks,
                tileDeck,
                ObjectiveDeck.makeTilePatternObjectiveDeck(),
                ObjectiveDeck.makeBambooSizeObjectiveDeck(),
                ObjectiveDeck.makeHarvestingObjectiveDeck());
    }

    public GameInventory(
            int irrigationSticks,
            TileDeck tileDeck,
            ObjectiveDeck<TilePatternObjective> tilePatternObjectiveDeck,
            ObjectiveDeck<BambooSizeObjective> bambooSizeObjectiveDeck,
            ObjectiveDeck<HarvestingObjective> harvestingObjectiveDeck) {
        this.irrigationSticks = irrigationSticks;
        this.tileDeck = tileDeck;
        this.tilePatternObjectiveDeck = tilePatternObjectiveDeck;
        this.bambooSizeObjectiveDeck = bambooSizeObjectiveDeck;
        this.harvestingObjectiveDeck = harvestingObjectiveDeck;
    }

    public void decrementIrrigation() throws GameInventoryException {
        if (irrigationSticks == 0) {
            throw new GameInventoryException("No irrigation sticks left");
        }

        irrigationSticks--;
    }

    public TileDeck getTileDeck() {
        return tileDeck;
    }

    public ObjectiveDeck<TilePatternObjective> getTilePatternObjectiveDeck() {
        return tilePatternObjectiveDeck;
    }

    public ObjectiveDeck<BambooSizeObjective> getBambooSizeObjectiveDeck() {
        return bambooSizeObjectiveDeck;
    }

    public ObjectiveDeck<HarvestingObjective> getHarvestingObjectiveDeck() {
        return harvestingObjectiveDeck;
    }

    public boolean hasIrrigation() {
        return irrigationSticks > 0;
    }
}
