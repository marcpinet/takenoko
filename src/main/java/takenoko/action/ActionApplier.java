package takenoko.action;

import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.tile.Color;
import takenoko.game.tile.TileDeck;
import takenoko.player.Inventory;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.utils.Coord;

public class ActionApplier {
    private final Board board;
    private final Logger out;
    private final GameInventory gameInventory;
    private final TileDeck tileDeck;

    public ActionApplier(Board board, Logger out, GameInventory gameInventory, TileDeck tileDeck) {
        this.board = board;
        this.out = out;
        this.gameInventory = gameInventory;
        this.tileDeck = tileDeck;
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    // S131: we're using pattern matching, so we don't need a default branch
    @SuppressWarnings({"java:S1301", "java:S1481", "java:S131", "DuplicateBranchesInSwitch"})
    public boolean apply(Action action, Player player) {
        return switch (action) {
            case Action.None ignored -> false;
            case Action.EndTurn ignored -> false;
            case Action.PlaceTile placeTile -> apply(placeTile);
            case Action.UnveilObjective unveilObjective -> apply(player, unveilObjective);
            case Action.TakeIrrigationStick ignored -> apply(player);
            case Action.PlaceIrrigationStick placeIrrigationStick -> apply(
                    player, placeIrrigationStick);
            case Action.MoveGardener moveGardener -> apply(
                    MovablePiece.GARDENER, moveGardener.coord());
            case Action.MovePanda movePanda -> apply(MovablePiece.PANDA, movePanda.coord());
        };
    }

    private boolean apply(MovablePiece piece, Coord pieceCoord) {
        try {
            this.board.move(piece, pieceCoord);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    private boolean apply(Player player, Action.PlaceIrrigationStick placeIrrigationStick) {
        try {
            player.getInventory().decrementIrrigation();
            board.placeIrrigation(placeIrrigationStick.coord(), placeIrrigationStick.side());
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    private boolean apply(Player player) {
        try {
            gameInventory.decrementIrrigation();
            player.getInventory().incrementIrrigation();
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    private boolean apply(Player player, Action.UnveilObjective unveilObjective) {
        if (unveilObjective.objective() instanceof HarvestingObjective needs) {
            Inventory inventory = player.getInventory();
            try {
                inventory.useBamboo(Color.GREEN, needs.getGreen());
                inventory.useBamboo(Color.YELLOW, needs.getYellow());
                inventory.useBamboo(Color.PINK, needs.getPink());
            } catch (InventoryException e) {
                this.out.log(Level.SEVERE, e.getMessage());
            }
        }
        return true;
    }

    private boolean apply(Action.PlaceTile placeTile) {
        try {
            var tile = tileDeck.draw(placeTile.drawTilePredicate());
            board.placeTile(placeTile.coord(), tile);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }
}
