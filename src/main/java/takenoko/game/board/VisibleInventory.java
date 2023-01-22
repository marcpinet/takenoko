package takenoko.game.board;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import takenoko.game.objective.Objective;
import takenoko.game.tile.Color;
import takenoko.game.tile.PowerUp;
import takenoko.player.InventoryException;

public class VisibleInventory {

    private final EnumMap<Color, Integer> bamboos;
    private int irrigations;
    private final EnumMap<PowerUp, Integer> powerUps;
    private final List<Objective> finishedObjectives;

    public VisibleInventory() {
        bamboos = new EnumMap<>(Color.class);
        irrigations = 0;
        powerUps = new EnumMap<>(PowerUp.class);
        finishedObjectives = new ArrayList<>();
    }

    public int getBamboo(Color color) {
        return bamboos.getOrDefault(color, 0);
    }

    public void incrementBamboo(Color color) {
        bamboos.put(color, getBamboo(color) + 1);
    }

    public void useBamboo(Color color, int nb) throws InventoryException {
        if (getBamboo(color) < nb) {
            throw new InventoryException("Not enough bamboo");
        }
        bamboos.put(color, getBamboo(color) - nb);
    }

    public boolean hasIrrigation() {
        return irrigations > 0;
    }

    public void incrementIrrigation() {
        irrigations++;
    }

    public void decrementIrrigation() throws InventoryException {
        if (!hasIrrigation()) {
            throw new InventoryException("Not enough irrigation");
        }
        irrigations--;
    }

    public boolean hasPowerUp(PowerUp powerUp) {
        return powerUps.getOrDefault(powerUp, 0) > 0;
    }

    public void incrementPowerUp(PowerUp powerUp) {
        powerUps.put(powerUp, powerUps.getOrDefault(powerUp, 0) + 1);
    }

    public void decrementPowerUp(PowerUp powerUp) throws InventoryException {
        if (!hasPowerUp(powerUp)) {
            throw new InventoryException("Not enough power up");
        }
        powerUps.put(powerUp, powerUps.get(powerUp) - 1);
    }

    public void addObjective(Objective objective) {
        finishedObjectives.add(objective);
    }

    public List<Objective> getFinishedObjectives() {
        return finishedObjectives;
    }
}