package takenoko.player.bot;

import java.util.List;
import java.util.Random;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.player.PlayerBase;
import takenoko.utils.Utils;

/** A bot that chooses actions randomly. */
public class RandomBot extends PlayerBase<RandomBot> implements PlayerBase.PlayerBaseInterface {
    private final Random randomSource;

    public RandomBot(Random randomSource, String name) {
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

        // It is possible that there are no possible actions (3 actions, objective inventory full)
        return Utils.randomPick(possibleActions, randomSource).orElse(Action.END_TURN);
    }

    @Override
    public WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers) {
        return Utils.randomPick(allowedWeathers, randomSource)
                .orElseThrow(() -> new IllegalStateException("No possible weather"));
    }
}
