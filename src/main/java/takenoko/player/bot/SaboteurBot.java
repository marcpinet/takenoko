package takenoko.player.bot;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.tile.BambooTile;
import takenoko.player.PlayerBase;
import takenoko.utils.Utils;

public class SaboteurBot extends PlayerBase<SaboteurBot> implements PlayerBase.PlayerBaseInterface {
    private final Random randomSource;

    public SaboteurBot(Random randomSource, String name) {
        super(name);
        this.randomSource = randomSource;
    }

    @Override
    public Action chooseActionImpl(Board board, PossibleActionLister actionLister) {
        // if we do not have enough action credits, end the turn
        if (availableActionCredits() == 0) return Action.END_TURN;

        var possibleActions = actionLister.getPossibleActions();

        var bambooAction = retrieveBamboo(board, possibleActions);
        if (bambooAction.isPresent()) {
            return bambooAction.get();
        }

        return Utils.randomPick(possibleActions, randomSource).orElse(Action.END_TURN);
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

    @Override
    public WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers) {
        return Utils.randomPick(allowedWeathers, randomSource)
                .orElseThrow(() -> new IllegalStateException("No possible weather"));
    }
}
