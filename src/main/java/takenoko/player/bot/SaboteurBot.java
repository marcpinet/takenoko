package takenoko.player.bot;

import java.util.*;
import java.util.function.Predicate;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.Objective;
import takenoko.game.objective.Objective.Status;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.PowerUp;
import takenoko.player.PlayerBase;
import takenoko.utils.Utils;

public class SaboteurBot extends PlayerBase<SaboteurBot> implements PlayerBase.PlayerBaseInterface {
    private static final List<Class<? extends Action>> PRIORITIES =
            List.of(Action.TakeObjective.class, Action.TakeIrrigationStick.class);

    private final Random randomSource;
    private final LinkedHashMap<Action, LinkedHashMap<Objective, Status>> simulationResults =
            new LinkedHashMap<>();
    private List<Objective> focusedObjectives;
    private boolean previousWasSimulation = false;

    public SaboteurBot(Random randomSource, String name) {
        super(name);
        this.randomSource = randomSource;
    }

    @Override
    public Action chooseActionImpl(Board board, PossibleActionLister actionLister) {
        var possibleActions = actionLister.getPossibleActions();

        // We have to clear the simulation results if we did not play the simulation result first
        // Otherwise we could end up playing a simulation result from a previous turn
        Optional<Action> simulationBestAction = Optional.empty();
        // We also have to check if the simulation result was bad, because if it was, we should not
        // try to simulate again
        boolean simulationResultWasBad = false;
        if (previousWasSimulation) {
            simulationBestAction = applySimulationResult();
            simulationResultWasBad = simulationBestAction.isEmpty();

            previousWasSimulation = false;
            simulationResults.clear();
        }

        var pickWaterPowerUp = pickWaterShedPowerUp(possibleActions);
        if (pickWaterPowerUp.isPresent()) {
            return pickWaterPowerUp.get();
        }

        var priorityAction = pickPriorityAction(possibleActions);
        if (priorityAction.isPresent()) {
            return priorityAction.get();
        }

        var bambooAction = retrieveBamboo(board, possibleActions);
        if (bambooAction.isPresent()) {
            return bambooAction.get();
        }

        selectFocusedObjectives();

        if (simulationBestAction.isPresent()) {
            return simulationBestAction.get();
        }

        var simulateNextAction = simulateNextAction(possibleActions);
        if (!simulationResultWasBad && simulateNextAction.isPresent()) {
            return simulateNextAction.get();
        }
        return pickRandomAction(possibleActions);
    }

    private Optional<Action> pickPriorityAction(List<Action> possibleActions) {
        for (var priority : PRIORITIES) {
            var found = possibleActions.stream().filter(priority::isInstance).findFirst();
            if (found.isPresent()) {
                return found;
            }
        }
        return Optional.empty();
    }

    private Optional<Action.MovePiece> retrieveBamboo(Board board, List<Action> possibleActions) {
        Predicate<Action.MovePiece> wouldGiveBamboo =
                movePiece -> {
                    try {
                        var tile = board.getTile(movePiece.to());
                        return tile instanceof BambooTile bambooTile
                                && bambooTile.getBambooSize() > 0;
                    } catch (BoardException e) {
                        throw new IllegalStateException(e);
                    }
                };

        var bambooActions =
                possibleActions.stream()
                        .filter(Action.MovePiece.class::isInstance)
                        .map(action -> (Action.MovePiece) action)
                        .filter(movePiece -> movePiece.piece() == MovablePiece.PANDA)
                        .filter(wouldGiveBamboo);

        return bambooActions.findFirst();
    }

    private Optional<Action.PickPowerUp> pickWaterShedPowerUp(List<Action> possibleActions) {
        return possibleActions.stream()
                .filter(Action.PickPowerUp.class::isInstance)
                .map(action -> (Action.PickPowerUp) action)
                .filter(pickPowerUp -> pickPowerUp.powerUp() == PowerUp.WATERSHED)
                .findFirst();
    }

    private void selectFocusedObjectives() {
        var objectives = getPrivateInventory().getObjectives();
        // pick the two objectives with the highest score
        focusedObjectives =
                objectives.stream()
                        .sorted(Comparator.comparing(Objective::getScore).reversed())
                        .limit(2)
                        .toList();
    }

    private Optional<Action> applySimulationResult() {
        if (!simulationResults.isEmpty()) {
            // let's find the action that will make us progress the most on our focused objectives
            var ret = bestActionsFromSimulation(simulationResults, focusedObjectives);

            var best = ret.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));

            // if the best action doesn't make us progress, we don't do it
            if (best.isPresent() && best.get().getValue() > 0) {
                return best.map(Map.Entry::getKey);
            }
        }
        return Optional.empty();
    }

    private Optional<Action> simulateNextAction(List<Action> possibleActions) {
        if (!possibleActions.isEmpty()) {
            previousWasSimulation = true;
            return Optional.of(new Action.SimulateActions(possibleActions, simulationResults));
        }

        return Optional.empty();
    }

    private Action pickRandomAction(List<Action> possibleActions) {
        return Utils.randomPick(possibleActions, randomSource).orElse(Action.END_TURN);
    }

    @Override
    public WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers) {
        if (allowedWeathers.contains(WeatherDice.Face.RAIN)) {
            return WeatherDice.Face.RAIN;
        }

        return Utils.randomPick(allowedWeathers, randomSource)
                .orElseThrow(() -> new IllegalStateException("No possible weather"));
    }
}
