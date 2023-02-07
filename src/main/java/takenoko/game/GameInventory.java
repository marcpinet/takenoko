package takenoko.game;

import java.util.Random;
import takenoko.game.objective.ObjectiveDeck;
import takenoko.game.tile.PowerUpReserve;
import takenoko.game.tile.TileDeck;

public class GameInventory {
    private final ObjectiveDeck tilePatternObjectiveDeck;
    private final ObjectiveDeck bambooSizeObjectiveDeck;
    private final ObjectiveDeck harvestingObjectiveDeck;
    private final PowerUpReserve powerUpReserve;
    private int irrigationSticks;
    private final TileDeck tileDeck;

    public GameInventory(int irrigationSticks, TileDeck tileDeck, Random random) {
        this(
                irrigationSticks,
                tileDeck,
                ObjectiveDeck.makeTilePatternObjectiveDeck(random),
                ObjectiveDeck.makeBambooSizeObjectiveDeck(random),
                ObjectiveDeck.makeHarvestingObjectiveDeck(random),
                new PowerUpReserve());
    }

    public GameInventory(
            int irrigationSticks,
            TileDeck tileDeck,
            ObjectiveDeck tilePatternObjectiveDeck,
            ObjectiveDeck bambooSizeObjectiveDeck,
            ObjectiveDeck harvestingObjectiveDeck,
            PowerUpReserve powerUpReserve) {
        this.irrigationSticks = irrigationSticks;
        this.tileDeck = tileDeck;
        this.tilePatternObjectiveDeck = tilePatternObjectiveDeck;
        this.bambooSizeObjectiveDeck = bambooSizeObjectiveDeck;
        this.harvestingObjectiveDeck = harvestingObjectiveDeck;
        this.powerUpReserve = powerUpReserve;
    }

    public void decrementIrrigation() throws GameInventoryException {
        if (irrigationSticks == 0) {
            throw new GameInventoryException("No irrigation sticks left");
        }

        irrigationSticks--;
    }

    public void incrementIrrigation() {
        irrigationSticks++;
    }

    public TileDeck getTileDeck() {
        return tileDeck;
    }

    public ObjectiveDeck getTilePatternObjectiveDeck() {
        return tilePatternObjectiveDeck;
    }

    public ObjectiveDeck getBambooSizeObjectiveDeck() {
        return bambooSizeObjectiveDeck;
    }

    public ObjectiveDeck getHarvestingObjectiveDeck() {
        return harvestingObjectiveDeck;
    }

    public PowerUpReserve getPowerUpReserve() {
        return powerUpReserve;
    }

    public boolean hasIrrigation() {
        return irrigationSticks > 0;
    }
}
