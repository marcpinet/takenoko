package takenoko;

import java.util.ArrayList;
import java.util.List;
import takenoko.objective.Objective;

@SuppressWarnings("java:S119") // Why couldn't I name my template SELF?
public abstract class PlayerBase<SELF extends PlayerBase<SELF> & PlayerBase.PlayerBaseInterface>
        implements Player {
    private final SELF self;
    private final List<Objective> objectives;
    private int actionCredits = 0;

    @SuppressWarnings("unchecked")
    public PlayerBase() {
        // SAFETY: This is safe because we're an abstract class using CRTP
        self = (SELF) this;
        objectives = new ArrayList<>();
    }

    @Override
    public void addObjective(Objective objective) {
        objectives.add(objective);
    }

    @Override
    public List<Objective> getObjectives() {
        return objectives;
    }

    public void beginTurn(int actionCredits) {
        this.actionCredits = actionCredits;
    }

    public int availableActionCredits() {
        return actionCredits;
    }

    @Override
    public Action chooseAction(Board board) {
        var action = self.chooseActionImpl(board);
        actionCredits -= action.cost();
        if (actionCredits < 0) {
            throw new IllegalStateException("Not enough action credits");
        }
        return action;
    }

    public boolean wantsToEndTurn() {
        return availableActionCredits() == 0;
    }

    public interface PlayerBaseInterface {
        Action chooseActionImpl(Board board);
    }
}
