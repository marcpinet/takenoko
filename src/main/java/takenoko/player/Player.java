package takenoko.player;

import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

public interface Player {
    void beginTurn(int actionCredits);

    int availableActionCredits();

    Action chooseAction(Board board, PossibleActionLister actionLister) throws PlayerException;

    PrivateInventory getPrivateInventory();

    VisibleInventory getVisibleInventory();

    void increaseScore(int delta);

    int getScore();

    void decreaseScore(int score);
}
