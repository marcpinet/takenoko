package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

public interface Objective {
    record Status(int completed, int totalToComplete) {
        public boolean achieved() {
            return completed >= totalToComplete;
        }

        public float progressFraction() {
            return (float) completed / totalToComplete;
        }
    }

    Status computeAchieved(Board board, Action lastAction, VisibleInventory visibleInventory);

    Status status();

    default boolean isAchieved() {
        return status().achieved();
    }

    int getScore();
}
