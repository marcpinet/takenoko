package takenoko.game.objective;

import java.util.EnumMap;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.Color;

public final class HarvestingObjective implements Objective {
    private final EnumMap<Color, Integer> needs;
    private final int score;
    private Status status;

    public HarvestingObjective(int green, int yellow, int pink, int score) {
        this.needs = new EnumMap<>(Color.class);
        this.needs.put(Color.GREEN, green);
        this.needs.put(Color.YELLOW, yellow);
        this.needs.put(Color.PINK, pink);
        this.score = score;
        resetStatus(0);
    }

    public HarvestingObjective(int green, int yellow, int pink) {
        this(green, yellow, pink, 1);
    }

    private Status resetStatus(int completion) {
        var totalNumberOfBamboosRequired =
                needs.values().stream().mapToInt(Integer::intValue).sum();
        status = new Status(completion, totalNumberOfBamboosRequired);
        return status;
    }

    public Status computeAchieved(
            Board ignoredB, Action ignoredA, VisibleInventory visibleInventory) {
        resetStatus(0);
        var toComplete = status.totalToComplete();
        for (var color : Color.values()) {
            var numberOfBamboos = visibleInventory.getBamboo(color);
            var numberOfBamboosRequired = needs.get(color);
            if (numberOfBamboos < numberOfBamboosRequired) {
                toComplete -= numberOfBamboosRequired - numberOfBamboos;
            }
        }
        return resetStatus(toComplete);
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public void forceRecomputeOnNextCheck() {
        // Nothing to do
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public Type getType() {
        return Type.HARVESTING;
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
