package takenoko.player;

import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;

public interface Player {
    void beginTurn(int actionCredits);

    int availableActionCredits();

    Action chooseAction(Board board, ActionValidator actionValidator) throws PlayerException;

    Inventory getInventory();

    void increaseScore(int delta);

    int getScore();
}
