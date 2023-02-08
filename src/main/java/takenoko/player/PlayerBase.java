package takenoko.player;

import java.util.List;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

@SuppressWarnings("java:S119") // Why couldn't I name my template SELF?
public abstract class PlayerBase<SELF extends PlayerBase<SELF> & PlayerBase.PlayerBaseInterface>
        implements Player {
    private final SELF self;
    private final PrivateInventory privateInventory;
    private final VisibleInventory visibleInventory;
    private int actionCredits = 0;
    private int score = 0;
    private String name;

    @SuppressWarnings("unchecked")
    protected PlayerBase(String name) {
        // SAFETY: This is safe because we're an abstract class using CRTP
        self = (SELF) this;
        privateInventory = new PrivateInventory();
        visibleInventory = new VisibleInventory();
        this.name = name;
    }

    protected PlayerBase() {
        this("Unnamed");
    }

    @Override
    public PrivateInventory getPrivateInventory() {
        return privateInventory;
    }

    public VisibleInventory getVisibleInventory() {
        return visibleInventory;
    }

    public void beginTurn(int actionCredits) {
        this.actionCredits = actionCredits;
    }

    public int availableActionCredits() {
        return actionCredits;
    }

    @Override
    public Action chooseAction(Board board, PossibleActionLister actionLister) {
        var action = self.chooseActionImpl(board, actionLister);

        actionCredits -= action.hasCost() ? 1 : 0;
        if (actionCredits < 0) {
            throw new IllegalStateException("Not enough action credits");
        }
        return action;
    }

    @Override
    public WeatherDice.Face chooseWeather(List<WeatherDice.Face> allowedWeathers) {
        var weather = self.chooseWeatherImpl(allowedWeathers);
        if (!allowedWeathers.contains(weather)) {
            throw new IllegalStateException("Not allowed weather");
        }
        return weather;
    }

    @Override
    public void increaseScore(int delta) {
        score += delta;
    }

    @Override
    public void decreaseScore(int delta) {
        score -= delta;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getName() {
        return name;
    }

    public interface PlayerBaseInterface {
        Action chooseActionImpl(Board board, PossibleActionLister actionLister);

        WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers);
    }
}
