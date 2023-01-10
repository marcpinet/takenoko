package takenoko.action;

import java.util.ArrayList;
import java.util.List;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.tile.Color;
import takenoko.game.tile.TileDeck;
import takenoko.player.Inventory;

public class ActionValidator {
    private final Board board;
    private final TileDeck deck;
    private final GameInventory gameInventory;
    private final Inventory playerInventory;
    private final List<Action> alreadyPlayedActions;

    public ActionValidator(
            Board board,
            TileDeck deck,
            GameInventory gameInventory,
            Inventory playerInventory,
            List<Action> alreadyPlayedActions) {
        this.board = board;
        this.deck = deck;
        this.gameInventory = gameInventory;
        this.playerInventory = playerInventory;
        this.alreadyPlayedActions = alreadyPlayedActions;
    }

    public ActionValidator(
            Board board, TileDeck deck, GameInventory gameInventory, Inventory playerInventory) {
        this(board, deck, gameInventory, playerInventory, new ArrayList<>());
    }

    public boolean isValid(Action action) {
        for (Action a : alreadyPlayedActions) if (a.isSameTypeAs(action)) return false;

        return switch (action) {
            case Action.None ignored -> true;
            case Action.PlaceIrrigationStick a -> isValid(a);
            case Action.PlaceTile a -> isValid(a);
            case Action.TakeIrrigationStick a -> isValid(a);
            case Action.UnveilObjective a -> isValid(a);
            case Action.MoveGardener a -> isValid(a);
            case Action.MovePanda a -> isValid(a);
            case Action.EndTurn ignored -> true;
        };
    }

    private boolean isValid(Action.PlaceIrrigationStick action) {
        if (!playerInventory.hasIrrigation()) {
            return false;
        }

        var coord = action.coord();
        var side = action.side();

        try {
            var tile = board.getTile(coord);
            return !tile.isSideIrrigated(side);
        } catch (BoardException e) {
            return false;
        }
    }

    private boolean isValid(Action.PlaceTile action) {
        return deck.size() > 0 && board.isAvailableCoord(action.coord());
    }

    private boolean isValid(Action.TakeIrrigationStick action) {
        return gameInventory.hasIrrigation();
    }

    private boolean isValid(Action.UnveilObjective action) {
        if (!action.objective().wasAchievedAfterLastCheck()) return false;

        if (action.objective() instanceof HarvestingObjective needs) {
            return playerInventory.getBamboo(Color.GREEN) >= needs.getGreen()
                    && playerInventory.getBamboo(Color.PINK) >= needs.getPink()
                    && playerInventory.getBamboo(Color.YELLOW) >= needs.getYellow();
        }
        return true;
    }

    private boolean isValid(Action.MoveGardener action) {
        return board.getPlacedCoords().contains(action.coord())
                && board.getGardenerCoord().isAlignedWith(action.coord());
    }

    private boolean isValid(Action.MovePanda action) {
        return board.getPlacedCoords().contains(action.coord())
                && board.getPandaCoord().isAlignedWith(action.coord());
    }
}
