package takenoko.game;

import takenoko.game.tile.TileDeck;

public class GameInventory {
    int irrigationSticks;
    TileDeck tileDeck;

    public GameInventory(int irrigationSticks, TileDeck tileDeck) {
        this.irrigationSticks = irrigationSticks;
        this.tileDeck = tileDeck;
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

    public boolean hasIrrigation() {
        return irrigationSticks > 0;
    }
}
