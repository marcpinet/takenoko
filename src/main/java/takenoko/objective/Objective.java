package takenoko.objective;

import takenoko.Action;
import takenoko.Board;

public interface Objective {
    boolean isAchieved(Board board, Action lastAction);
}
