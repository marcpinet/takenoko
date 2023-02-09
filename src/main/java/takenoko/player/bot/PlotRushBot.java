package takenoko.player.bot;

import java.util.*;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.objective.Objective;
import takenoko.player.PlayerBase;
import takenoko.utils.Utils;

public class PlotRushBot extends PlayerBase<PlotRushBot> implements PlayerBase.PlayerBaseInterface {
    private final Random randomSource;
    HashMap<Action, Map<Objective, Objective.Status>> outStatuses = new HashMap<>();

    public PlotRushBot(Random randomSource, String name) {
        super(name);
        this.randomSource = randomSource;
    }

    public Action chooseActionImpl(Board board, PossibleActionLister actionLister) {
        var possibleActions = actionLister.getPossibleActions();
        // If an objective is achieved, unveil it
        for (var action : possibleActions) {
            if (action instanceof Action.UnveilObjective) {
                return action;
            }
        }

        // if we do not have enough action credits, end the turn
        if (availableActionCredits() == 0) return Action.END_TURN;

        // Starting by drawing a lot of tile pattern objectives
        for (var action : possibleActions) {
            if (action instanceof Action.TakeObjective placeTile
                    && placeTile.type() == Objective.Type.TILE_PATTERN) {
                return action;
            }
        }

        // And get all the possible Place Tile actions and Place Irrigation Stick actions
        List<Action> possiblePlaceTilesAndIrrigations = new ArrayList<>();
        for (var action : possibleActions) {
            if (action instanceof Action.PlaceTile
                    || action instanceof Action.PlaceIrrigationStick) {
                possiblePlaceTilesAndIrrigations.add(action);
            }
        }

        // Checking best action to take
        Optional<Action> simulation =
                this.simulateBestPlotRushMove(possiblePlaceTilesAndIrrigations);
        if (simulation.isPresent()) return simulation.get();

        // If no irrigation stick nor place tile would be beneficial, take one
        for (var action : possibleActions) {
            if (action instanceof Action.TakeIrrigationStick) {
                return action;
            }
        }

        // else if we have no advantageous tile to place, take an irrigation stick (or place one)
        for (var action : possibleActions) {
            if (action instanceof Action.PlaceIrrigationStick
                    || action instanceof Action.TakeIrrigationStick) {
                return action;
            }
        }

        // else, just place a tile
        for (var action : possibleActions) {
            if (action instanceof Action.PlaceTile) {
                return action;
            }
        }

        // else, play a random action
        return Utils.randomPick(possibleActions, randomSource).orElse(Action.END_TURN);
    }

    public Optional<Action> simulateBestPlotRushMove(
            List<Action> possiblePlaceTilesAndIrrigations) {
        if (outStatuses.isEmpty() && !possiblePlaceTilesAndIrrigations.isEmpty())
            return Optional.of(
                    new Action.SimulateActions(possiblePlaceTilesAndIrrigations, outStatuses));

        if (!outStatuses.isEmpty()) {
            var actionToObjectiveProgress = getObjectiveProgressFromSimulation(outStatuses);

            Action bestAction = null;
            double bestProgress = 0;
            for (var entry : actionToObjectiveProgress.entrySet()) {
                var action = entry.getKey();
                for (var mapObjectiveStatus : entry.getValue().entrySet()) {
                    var progress = mapObjectiveStatus.getValue();
                    if (progress > bestProgress) {
                        bestProgress = progress;
                        bestAction = action;
                    }
                }
            }

            outStatuses.clear();
            return Optional.ofNullable(bestAction);
        }
        return Optional.empty();
    }

    @Override
    public WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers) {
        // Tries to take SUN. If he can't, take CLOUD else WIND and finally RANDOM
        if (allowedWeathers.contains(WeatherDice.Face.SUN)) return WeatherDice.Face.SUN;
        if (allowedWeathers.contains(WeatherDice.Face.CLOUDY)) return WeatherDice.Face.CLOUDY;
        if (allowedWeathers.contains(WeatherDice.Face.WIND)) return WeatherDice.Face.WIND;
        return Utils.randomPick(allowedWeathers, randomSource).orElse(null);
    }
}
