package takenoko.player.bot;

import java.util.Random;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.board.Board;
import takenoko.game.tile.TileDeck;
import takenoko.player.PlayerBase;
import takenoko.utils.Utils;

/** A bot that chooses actions randomly. */
public class EasyBot extends PlayerBase<EasyBot> implements PlayerBase.PlayerBaseInterface {
    private final Random randomSource;

    public EasyBot(Random randomSource) {
        this.randomSource = randomSource;
    }

    public Action chooseActionImpl(Board board, PossibleActionLister actionLister) {
        var possibleActions = actionLister.getPossibleActions(TileDeck.DEFAULT_DRAW_PREDICATE);
        // If an objective is achieved, unveil it
        for (var action : possibleActions) {
            if (action instanceof Action.UnveilObjective) {
                return action;
            }
        }

        // if we do not have enough action credits, end the turn
        if (availableActionCredits() == 0) return Action.END_TURN;

        return Utils.randomPick(possibleActions, randomSource)
                .orElseThrow(() -> new IllegalStateException("No possible action"));
    }
}
