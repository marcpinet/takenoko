package takenoko;

import java.util.List;
import takenoko.objective.Objective;

public interface Player {
    void addObjective(Objective objective);

    List<Objective> getObjectives();

    void beginTurn(int actionCredits);

    int availableActionCredits();

    Action chooseAction(Board board) throws PlayerException;

    boolean wantsToEndTurn();

    int getInventory();

    void takeIrrigationStick();

    void placeIrrigationStick();
}
