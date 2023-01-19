package takenoko.player;

import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.board.Board;

public interface Player {
    void beginTurn(int actionCredits);

    int availableActionCredits();

    Action chooseAction(Board board, PossibleActionLister actionLister) throws PlayerException;

    Inventory getInventory();

    void increaseScore(int delta);

    int getScore();
}
