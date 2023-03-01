package takenoko.action;

import java.util.ArrayList;
import java.util.List;
import takenoko.game.Deck;
import takenoko.game.board.Board;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.Objective;
import takenoko.game.tile.PowerUp;
import takenoko.game.tile.Tile;
import takenoko.game.tile.TileSide;
import takenoko.player.PrivateInventory;

public class PossibleActionLister {
    private final Board board;
    private final ActionValidator validator;
    private final PrivateInventory playerPrivateInventory;

    public PossibleActionLister(
            Board board, ActionValidator validator, PrivateInventory playerPrivateInventory) {
        this.board = board;
        this.validator = validator;
        this.playerPrivateInventory = playerPrivateInventory;
    }

    private List<Action> getActionsExceptPlaceTile() {
        List<Action> possibleActions = new ArrayList<>();

        for (var objectiveType : Objective.Type.values()) {
            possibleActions.add(new Action.TakeObjective(objectiveType));
        }

        for (var powerUp : PowerUp.values()) {
            if (powerUp != PowerUp.NONE) {
                possibleActions.add(new Action.PickPowerUp(powerUp));
                for (var coord : board.getPlacedCoords()) {
                    possibleActions.add(new Action.PlacePowerUp(coord, powerUp));
                }
            }
        }

        for (var coord : board.getPlacedCoords()) {
            possibleActions.add(new Action.MovePiece(MovablePiece.GARDENER, coord));
            possibleActions.add(new Action.MovePiece(MovablePiece.PANDA, coord));

            possibleActions.add(new Action.GrowOneTile(coord));
            possibleActions.add(new Action.MovePandaAnywhere(coord));
        }

        possibleActions.add(new Action.TakeIrrigationStick());

        for (var coord : board.getPlacedCoords()) {
            for (var side : TileSide.values()) {
                possibleActions.add(new Action.PlaceIrrigationStick(coord, side));
            }
        }

        for (var objective : playerPrivateInventory.getObjectives()) {
            possibleActions.add(new Action.UnveilObjective(objective));
        }

        return possibleActions;
    }

    public List<Action> getPossibleActions() {
        List<Action> possibleActions = getActionsExceptPlaceTile();

        for (var coord : board.getAvailableCoords()) {
            possibleActions.add(new Action.PlaceTile(coord, tiles -> 0));
            possibleActions.add(new Action.PlaceTile(coord, tiles -> 1));
            possibleActions.add(new Action.PlaceTile(coord, tiles -> 2));
        }

        return possibleActions.stream().filter(validator::isValid).toList();
    }

    public List<Action> getPossibleActions(Deck.DrawPredicate<Tile> drawPredicate) {
        List<Action> possibleActions = getActionsExceptPlaceTile();

        for (var coord : board.getAvailableCoords()) {
            possibleActions.add(new Action.PlaceTile(coord, drawPredicate));
        }

        return possibleActions.stream().filter(validator::isValid).toList();
    }
}
