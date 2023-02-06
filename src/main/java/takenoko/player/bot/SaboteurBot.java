package takenoko.player.bot;

import java.util.Random;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.TileDeck;
import takenoko.player.PlayerBase;
import takenoko.utils.Utils;

public class SaboteurBot extends PlayerBase<SaboteurBot> implements PlayerBase.PlayerBaseInterface {
    private final Random randomSource;

    public SaboteurBot(Random randomSource) {
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

        // Getting a lot of objectives
        for (Action action : possibleActions) {
            if (action instanceof Action.TakeBambooSizeObjective
                    || action instanceof Action.TakeHarvestingObjective
                    || action instanceof Action.TakeIrrigationStick
                    || action instanceof Action.TakeTilePatternObjective) {
                return action;
            }
        }

        // Getting a lot of bamboos cut down
        for (Action action : possibleActions) {
            if (action instanceof Action.MovePiece movePiece
                    && movePiece.piece() == MovablePiece.PANDA) {
                try {
                    if (board.getTile(movePiece.to()) instanceof BambooTile bt
                            && bt.getBambooSize() > 0) {
                        return action;
                    }
                } catch (BoardException ignored) {
                    // ignore
                }
            }
        }

        return Utils.randomPick(possibleActions, randomSource)
                .orElseThrow(() -> new IllegalStateException("No possible action"));
    }
}
