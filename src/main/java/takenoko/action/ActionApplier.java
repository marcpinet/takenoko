package takenoko.action;

import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.tile.Color;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.utils.Coord;

public class ActionApplier {
    private final Board board;
    private final Logger out;
    private final GameInventory gameInventory;

    public ActionApplier(Board board, Logger out, GameInventory gameInventory) {
        this.board = board;
        this.out = out;
        this.gameInventory = gameInventory;
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    // S131: we're using pattern matching, so we don't need a default branch
    @SuppressWarnings({"java:S1301", "java:S1481", "java:S131", "DuplicateBranchesInSwitch"})
    public void apply(Action action, Player player) {
        switch (action) {
            case Action.None ignored -> {}
            case Action.EndTurn ignored -> {}
            case Action.PlaceTile placeTile -> apply(placeTile);
            case Action.UnveilObjective unveilObjective -> apply(player, unveilObjective);
            case Action.TakeIrrigationStick ignored -> apply(player);
            case Action.PlaceIrrigationStick placeIrrigationStick -> apply(
                    player, placeIrrigationStick);
            case Action.MoveGardener moveGardener -> apply(
                    MovablePiece.GARDENER, moveGardener.coord());
            case Action.MovePanda movePanda -> apply(MovablePiece.PANDA, movePanda.coord());
        }
    }

    private void apply(MovablePiece piece, Coord pieceCoord) {
        try {
            this.board.move(piece, pieceCoord);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Player player, Action.PlaceIrrigationStick placeIrrigationStick) {
        try {
            player.getInventory().decrementIrrigation();
            board.placeIrrigation(placeIrrigationStick.coord(), placeIrrigationStick.side());
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Player player) {
        try {
            gameInventory.decrementIrrigation();
            player.getInventory().incrementIrrigation();
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Player player, Action.UnveilObjective unveilObjective) {
        var inventory = player.getInventory();
        try {
            if (unveilObjective.objective() instanceof HarvestingObjective needs) {
                inventory.useBamboo(Color.GREEN, needs.getGreen());
                inventory.useBamboo(Color.YELLOW, needs.getYellow());
                inventory.useBamboo(Color.PINK, needs.getPink());
            }
            inventory.removeObjective(unveilObjective.objective());
            player.increaseScore(unveilObjective.objective().getScore());
        } catch (InventoryException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Action.PlaceTile placeTile) {
        try {
            var tile = gameInventory.getTileDeck().draw(placeTile.drawPredicate());
            board.placeTile(placeTile.coord(), tile);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }
}
