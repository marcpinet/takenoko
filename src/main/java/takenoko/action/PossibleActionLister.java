package takenoko.action;

import java.util.ArrayList;
import java.util.List;
import takenoko.game.board.Board;
import takenoko.game.tile.TileDeck;
import takenoko.game.tile.TileSide;
import takenoko.player.Inventory;

public class PossibleActionLister {
    private final Board board;
    private final ActionValidator validator;
    private final Inventory playerInventory;

    public PossibleActionLister(Board board, ActionValidator validator, Inventory playerInventory) {
        this.board = board;
        this.validator = validator;
        this.playerInventory = playerInventory;
    }

    public List<Action> getPossibleActions(TileDeck.DrawTilePredicate drawTilePredicate) {
        List<Action> possibleActions = new ArrayList<>();

        possibleActions.add(Action.NONE);
        possibleActions.add(Action.END_TURN);

        for (var coord : board.getPlacedCoords()) {
            possibleActions.add(new Action.MoveGardener(coord));
            possibleActions.add(new Action.MovePanda(coord));
        }

        possibleActions.add(new Action.TakeIrrigationStick());

        for (var coord : board.getPlacedCoords()) {
            for (var side : TileSide.values()) {
                possibleActions.add(new Action.PlaceIrrigationStick(coord, side));
            }
        }

        for (var objective : playerInventory.getObjectives()) {
            possibleActions.add(new Action.UnveilObjective(objective));
        }

        for (var coord : board.getAvailableCoords()) {
            possibleActions.add(new Action.PlaceTile(coord, drawTilePredicate));
        }

        return possibleActions.stream().filter(validator::isValid).toList();
    }
}
