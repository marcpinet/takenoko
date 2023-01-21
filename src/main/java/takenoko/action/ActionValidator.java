package takenoko.action;

import java.util.ArrayList;
import java.util.List;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.Objective;
import takenoko.game.objective.ObjectiveDeck;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.Color;
import takenoko.game.tile.PowerUp;
import takenoko.player.PrivateInventory;

public class ActionValidator {
    private final Board board;
    private final GameInventory gameInventory;
    private final PrivateInventory playerPrivateInventory;
    private final VisibleInventory playerVisibleInventory;
    private final List<Action> alreadyPlayedActions;

    public ActionValidator(
            Board board,
            GameInventory gameInventory,
            PrivateInventory playerPrivateInventory,
            VisibleInventory playerVisibleInventory,
            List<Action> alreadyPlayedActions) {
        this.board = board;
        this.gameInventory = gameInventory;
        this.playerPrivateInventory = playerPrivateInventory;
        this.playerVisibleInventory = playerVisibleInventory;
        this.alreadyPlayedActions = alreadyPlayedActions;
    }

    public ActionValidator(
            Board board,
            GameInventory gameInventory,
            PrivateInventory playerPrivateInventory,
            VisibleInventory playerVisibleInventory) {
        this(
                board,
                gameInventory,
                playerPrivateInventory,
                playerVisibleInventory,
                new ArrayList<>());
    }

    public boolean isValid(Action action) {
        for (Action a : alreadyPlayedActions) if (a.isSameTypeAs(action)) return false;

        return switch (action) {
            case Action.None ignored -> true;
            case Action.PlaceIrrigationStick a -> isValid(a);
            case Action.PlaceTile a -> isValid(a);
            case Action.TakeIrrigationStick a -> isValid(a);
            case Action.TakeHarvestingObjective ignored -> canTakeObjective(
                    gameInventory.getHarvestingObjectiveDeck());
            case Action.TakeBambooSizeObjective ignored -> canTakeObjective(
                    gameInventory.getBambooSizeObjectiveDeck());
            case Action.TakeTilePatternObjective ignored -> canTakeObjective(
                    gameInventory.getTilePatternObjectiveDeck());
            case Action.UnveilObjective a -> isValid(a);
            case Action.MovePiece a -> isValid(a);
            case Action.EndTurn ignored -> true;
            case Action.PickPowerUp a -> isValid(a);
            case Action.PlacePowerUp a -> isValid(a);
        };
    }

    private <O extends Objective> boolean canTakeObjective(ObjectiveDeck<O> deck) {
        return deck.size() > 0 && playerPrivateInventory.canDrawObjective();
    }

    private boolean isValid(Action.PlaceIrrigationStick action) {
        if (!playerVisibleInventory.hasIrrigation()) {
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
        return gameInventory.getTileDeck().size() > 0 && board.isAvailableCoord(action.coord());
    }

    private boolean isValid(Action.TakeIrrigationStick action) {
        return gameInventory.hasIrrigation();
    }

    private boolean isValid(Action.UnveilObjective action) {
        if (!action.objective().isAchieved()) return false;

        if (action.objective() instanceof HarvestingObjective needs) {
            return playerVisibleInventory.getBamboo(Color.GREEN) >= needs.getGreen()
                    && playerVisibleInventory.getBamboo(Color.PINK) >= needs.getPink()
                    && playerVisibleInventory.getBamboo(Color.YELLOW) >= needs.getYellow();
        }
        return true;
    }

    private boolean isValid(Action.MovePiece action) {
        return board.getPlacedCoords().contains(action.coord())
                && board.getPieceCoord(action.piece()).isAlignedWith(action.coord())
                && !board.getPieceCoord(action.piece()).equals(action.coord());
    }

    private boolean isValid(Action.PickPowerUp action) {
        return gameInventory.getPowerUpReserve().canPick(action.powerUp());
    }

    private boolean isValid(Action.PlacePowerUp action) {
        try {
            return board.getTile(action.coord()) instanceof BambooTile bambooTile
                    && bambooTile.getPowerUp().equals(PowerUp.NONE)
                    && playerVisibleInventory.hasPowerUp(action.powerUp());
        } catch (BoardException ignored) {
        }
        return false;
    }
}
