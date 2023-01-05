package takenoko.game.objective;

import java.util.Arrays;
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
        achieved =
                Arrays.stream(Color.values())
                        .allMatch(color -> inventory.getBamboo(color) >= needs.get(color));
        return achieved;
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
