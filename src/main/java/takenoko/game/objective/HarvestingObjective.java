package takenoko.game.objective;

import java.util.Arrays;
import java.util.EnumMap;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.Color;

public class HarvestingObjective implements Objective {
    private final EnumMap<Color, Integer> needs;
    private boolean achieved = false;
    private final int score;

    public HarvestingObjective(int green, int yellow, int pink, int score) {
        this.needs = new EnumMap<>(Color.class);
        this.needs.put(Color.GREEN, green);
        this.needs.put(Color.YELLOW, yellow);
        this.needs.put(Color.PINK, pink);
        this.score = score;
    }

    public boolean computeAchieved(
            Board ignoredB, Action ignoredA, VisibleInventory visibleInventory) {
        achieved =
                Arrays.stream(Color.values())
                        .allMatch(color -> visibleInventory.getBamboo(color) >= needs.get(color));
        return achieved;
    }

    public boolean isAchieved() {
        return achieved;
    }

    @Override
    public int getScore() {
        return score;
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
