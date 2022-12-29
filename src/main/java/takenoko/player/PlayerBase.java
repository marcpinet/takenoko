package takenoko.player;

import java.util.ArrayList;
import java.util.List;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.objective.Objective;

@SuppressWarnings("java:S119") // Why couldn't I name my template SELF?
public abstract class PlayerBase<SELF extends PlayerBase<SELF> & PlayerBase.PlayerBaseInterface>
        implements Player {
    private final SELF self;
    private final List<Objective> objectives;
    private int actionCredits = 0;

    // TODO : Inventory should become a class with irrigations, power-ups, and objectives
    private int inventory;

    @SuppressWarnings("unchecked")
    public PlayerBase() {
        // SAFETY: This is safe because we're an abstract class using CRTP
        self = (SELF) this;
        objectives = new ArrayList<>();
        inventory = 0;
    }

    @Override
    public void addObjective(Objective objective) {
        objectives.add(objective);
    }

    @Override
    public List<Objective> getObjectives() {
        return objectives;
    }

    @Override
    public int getInventory() {
        return inventory;
    }

    @Override
    public void takeIrrigationStick() {
        inventory++;
    }

    @Override
    public void placeIrrigationStick() {
        inventory--;
    }

    public void beginTurn(int actionCredits) {
        this.actionCredits = actionCredits;
    }

    public int availableActionCredits() {
        return actionCredits;
    }

    @Override
    public Action chooseAction(Board board, ActionValidator actionValidator)
            throws PlayerException {
        return self.chooseActionImpl(board, actionValidator);
    }

    public void commitAction(Action action) {
        actionCredits -= action.cost();
        if (actionCredits < 0) {
            throw new IllegalStateException("Not enough action credits");
        }
    }

    public boolean wantsToEndTurn() {
        return availableActionCredits() == 0;
    }

    public interface PlayerBaseInterface {
        Action chooseActionImpl(Board board, ActionValidator validator) throws PlayerException;
    }
}