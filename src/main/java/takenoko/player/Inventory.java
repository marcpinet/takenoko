package takenoko.player;

import java.util.ArrayList;
import java.util.HashMap;
import takenoko.game.objective.Objective;
import takenoko.game.tile.Color;
import takenoko.game.tile.PowerUp;

public class Inventory {

    private HashMap<Color, Integer> bamboos;
    private int irrigations;
    private HashMap<PowerUp, Integer> powerUps;
    private ArrayList<Objective> objectives;

    public Inventory() {
        bamboos = new HashMap<>();
        irrigations = 0;
        powerUps = new HashMap<>();
        objectives = new ArrayList<>();
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

    public boolean canDrawObjective() {
        return objectives.size() < 5;
    }

    public void addObjective(Objective objective) throws InventoryException {
        if (!canDrawObjective()) {
            throw new InventoryException("Too many objectives");
        }
        objectives.add(objective);
    }

    public ArrayList<Objective> getObjectives() {
        return objectives;
    }
}
