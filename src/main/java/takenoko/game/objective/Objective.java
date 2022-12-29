package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;

public interface Objective {
    boolean isAchieved(Board board, Action lastAction);

    boolean wasAchievedAfterLastCheck();
}
