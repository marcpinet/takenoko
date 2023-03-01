package takenoko.player;

import java.util.ArrayList;
import java.util.List;
import takenoko.game.objective.Objective;

public class PrivateInventory {

    private final ArrayList<Objective> unfinishedObjectives;

    public PrivateInventory() {
        unfinishedObjectives = new ArrayList<>();
    }

    public boolean canDrawObjective() {
        return unfinishedObjectives.size() < 5;
    }

    public void addObjective(Objective objective) throws InventoryException {
        if (!canDrawObjective()) {
            throw new InventoryException("Too many objectives");
        }
        unfinishedObjectives.add(objective);
    }

    public void removeObjective(Objective objective) throws InventoryException {
        if (!unfinishedObjectives.remove(objective)) {
            throw new InventoryException("Objective not found");
        }
    }

    public List<Objective> getObjectives() {
        return unfinishedObjectives;
    }
}
