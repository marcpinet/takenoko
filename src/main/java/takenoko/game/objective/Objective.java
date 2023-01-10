package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.player.Inventory;

public interface Objective {
    boolean isAchieved(Board board, Action lastAction, Inventory inventory);

    boolean wasAchievedAfterLastCheck();

    int getScore();
}
