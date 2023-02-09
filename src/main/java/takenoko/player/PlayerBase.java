package takenoko.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.Objective;

@SuppressWarnings("java:S119") // Why couldn't I name my template SELF?
public abstract class PlayerBase<SELF extends PlayerBase<SELF> & PlayerBase.PlayerBaseInterface>
        implements Player {
    private final SELF self;
    private final PrivateInventory privateInventory;
    private final VisibleInventory visibleInventory;
    private int actionCredits = 0;
    private int score = 0;
    private final String name;

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

    protected Map<Action, Map<Objective, Double>> getObjectiveProgressFromSimulation(
            Map<Action, Map<Objective, Objective.Status>> simulationResults) {
        var actionToObjectiveProgress = new HashMap<Action, Map<Objective, Double>>();
        for (var actionToResults : simulationResults.entrySet()) {
            actionToObjectiveProgress.put(actionToResults.getKey(), new HashMap<>());
            for (var objectiveToStatus : actionToResults.getValue().entrySet()) {
                double progress =
                        objectiveToStatus.getValue().progressFraction()
                                - objectiveToStatus.getKey().status().progressFraction();
                actionToObjectiveProgress
                        .get(actionToResults.getKey())
                        .put(objectiveToStatus.getKey(), progress);
            }
        }
        return actionToObjectiveProgress;
    }

    /// given a list of actions, return the action that will make the most progress on the focused
    /// objectives
    protected Map<Action, Double> bestActionsFromSimulation(
            Map<Action, Map<Objective, Objective.Status>> simulationResults,
            List<Objective> focusedObjectives) {
        // let's first flatten the simulation results
        Map<Action, Map<Objective, Double>> actionToObjectiveProgress =
                getObjectiveProgressFromSimulation(simulationResults);

        // now we sum the progress for each action
        Map<Action, Double> simplifiedActions = new HashMap<>();
        for (var actionToResults : actionToObjectiveProgress.entrySet()) {
            double sum = 0;
            for (var objectiveProgress : actionToResults.getValue().entrySet()) {
                // we only care about the objectives we're focused on
                if (focusedObjectives.contains(objectiveProgress.getKey())) {
                    sum += objectiveProgress.getValue();
                }
            }
            simplifiedActions.put(actionToResults.getKey(), sum);
        }

        return simplifiedActions;
    }

    public interface PlayerBaseInterface {
        Action chooseActionImpl(Board board, PossibleActionLister actionLister);

        WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers);
    }
}
