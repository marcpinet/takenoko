package takenoko.game.objective;

import java.util.EnumMap;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.tile.Color;
import takenoko.player.Inventory;

public class HarvestingObjective implements Objective {
    private final EnumMap<Color, Integer> needs;
    private boolean achieved = false;

    public HarvestingObjective(int green, int yellow, int pink) {
        this.needs = new EnumMap<>(Color.class);
        this.needs.put(Color.GREEN, green);
        this.needs.put(Color.YELLOW, yellow);
        this.needs.put(Color.PINK, pink);
    }

    public boolean isAchieved(Board ignoredB, Action ignoredA, Inventory inventory) {
        for (Color color : Color.values()) {
            if (inventory.getBamboo(color) < needs.get(color)) {
                achieved = false;
                return false;
            }
        }
        achieved = true;
        return true;
    }

    public boolean wasAchievedAfterLastCheck() {
        return achieved;
    }

    public int getGreen() {
        return needs.get(Color.GREEN);
    }

    public int getYellow() {
        return needs.get(Color.YELLOW);
    }

    public int getPink() {
        return needs.get(Color.PINK);
    }
}
