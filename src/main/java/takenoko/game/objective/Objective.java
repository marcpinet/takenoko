package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

public interface Objective {
    boolean isAchieved(Board board, Action lastAction, VisibleInventory visibleInventory);

    boolean wasAchievedAfterLastCheck();

    int getScore();
}
