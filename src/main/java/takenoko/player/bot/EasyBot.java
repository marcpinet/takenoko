package takenoko.player.bot;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.tile.TileDeck;
import takenoko.game.tile.TileSide;
import takenoko.player.PlayerBase;
import takenoko.utils.Coord;
import takenoko.utils.Utils;

/** A bot that chooses actions randomly. */
public class EasyBot extends PlayerBase<EasyBot> implements PlayerBase.PlayerBaseInterface {
    private final Random randomSource;

    public EasyBot(Random randomSource) {
        this.randomSource = randomSource;
    }

    public Action chooseActionImpl(Board board, ActionValidator validator) {
        // If an objective is achieved, unveil it
        for (var obj : getObjectives())
            if (obj.wasAchievedAfterLastCheck()) return new Action.UnveilObjective(obj);

        final List<Function<Board, Action>> availableActions =
                List.of(this::placeTile, this::takeIrrigation, this::placeIrrigation);

        // Try at most MAX_TRIES before giving up
        final int MAX_TRIES = 100;
        for (int i = 0; i < MAX_TRIES; ++i) {
            var actionType = randomSource.nextInt(availableActions.size());
            var action = availableActions.get(actionType).apply(board);
            if (validator.isValid(action)) {
                return action;
            }
        }
        return Action.NONE;
    }

    private Action placeTile(Board board) {
        Set<Coord> availableCoords = board.getAvailableCoords();

        Coord coord = chooseRandom(availableCoords);

        return new Action.PlaceTile(coord, TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
    }

    private Action placeIrrigation(Board board) {
        var availableCoords = board.getPlacedCoords();

        Coord coord = chooseRandom(availableCoords);
        var side = randomSide();

        return new Action.PlaceIrrigationStick(coord, side);
    }

    private Action takeIrrigation(Board ignored) {
        return new Action.TakeIrrigationStick();
    }

    private Coord chooseRandom(Set<Coord> coords) {
        return Utils.randomPick(coords, randomSource).orElseThrow();
    }

    private TileSide randomSide() {
        return TileSide.values()[randomSource.nextInt(TileSide.values().length)];
    }
}
