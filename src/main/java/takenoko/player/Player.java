package takenoko.player;

import java.util.List;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.objective.Objective;

public interface Player {
    void addObjective(Objective objective);

    List<Objective> getObjectives();

    void beginTurn(int actionCredits);

    int availableActionCredits();

    Action chooseAction(Board board, ActionValidator actionValidator) throws PlayerException;

    boolean wantsToEndTurn();

    int getInventory();

    void takeIrrigationStick();

    void placeIrrigationStick();

    void commitAction(Action action);
}
