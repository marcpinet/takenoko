package takenoko.game;

public class GameInventory {
    int irrigationSticks;

    public GameInventory(int irrigationSticks) {
        this.irrigationSticks = irrigationSticks;
    }

    public void incrementIrrigation() {
        irrigationSticks++;
    }

    public void decrementIrrigation() throws GameInventoryException {
        if (irrigationSticks == 0) {
            throw new GameInventoryException("No irrigation sticks left");
        }

        irrigationSticks--;
    }

    public boolean hasIrrigation() {
        return irrigationSticks > 0;
    }
}
