package takenoko;

@SuppressWarnings("java:S119") // Why couldn't I name my template SELF?
public abstract class PlayerBase<SELF extends PlayerBase<SELF> & PlayerBase.PlayerBaseInterface>
        implements Player {
    public static interface PlayerBaseInterface {
        Action chooseActionImpl(Board board);
    }

    private final SELF self;
    private int actionCredits = 0;

    @SuppressWarnings("unchecked")
    public PlayerBase() {
        // SAFETY: This is safe because we're an abstract class using CRTP
        self = (SELF) this;
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
}
