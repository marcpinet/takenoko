package takenoko.action;

import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.Objective;
import takenoko.game.objective.ObjectiveDeck;
import takenoko.game.tile.*;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PrivateInventory;

public class ActionApplier {
    private final Board board;
    private final Logger out;
    private final GameInventory gameInventory;
    private final PrivateInventory playerPrivateInventory;

    public ActionApplier(
            Board board,
            Logger out,
            GameInventory gameInventory,
            PrivateInventory playerPrivateInventory) {
        this.board = board;
        this.out = out;
        this.gameInventory = gameInventory;
        this.playerPrivateInventory = playerPrivateInventory;
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
            case Action.MovePiece movePiece -> apply(movePiece, player);
            case Action.TakeBambooSizeObjective ignored -> drawObjective(
                    gameInventory.getBambooSizeObjectiveDeck());
            case Action.TakeHarvestingObjective ignored -> drawObjective(
                    gameInventory.getHarvestingObjectiveDeck());
            case Action.TakeTilePatternObjective ignored -> drawObjective(
                    gameInventory.getTilePatternObjectiveDeck());
            case Action.PickPowerUp pickPowerUp -> apply(player, pickPowerUp);
            case Action.PlacePowerUp placePowerUp -> apply(player, placePowerUp);

        }
    }

    private void apply(Player player, Action.PlacePowerUp placePowerUp) {
        try {
            player.getVisibleInventory().decrementPowerUp(placePowerUp.powerUp());

            var tile = (BambooTile) board.getTile(placePowerUp.coord());
            tile.setPowerUp(placePowerUp.powerUp());
        } catch (PowerUpException | InventoryException | BoardException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }



    private void apply(Player player, Action.PickPowerUp pickPowerUp) {
        try {
            player.getVisibleInventory().incrementPowerUp(pickPowerUp.powerUp());
            gameInventory.getPowerUpReserve().pick(pickPowerUp.powerUp());
        } catch (PowerUpNotAvailableException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private <O extends Objective> void drawObjective(ObjectiveDeck<O> objectiveDeck) {
        try {
            var obj = objectiveDeck.draw();
            playerPrivateInventory.addObjective(obj);
        } catch (EmptyDeckException e) {
            this.out.log(Level.SEVERE, "Objective deck is empty", e);
        } catch (InventoryException e) {
            this.out.log(Level.SEVERE, "Player inventory is full", e);
        }
    }

    private void apply(Action.MovePiece movePiece, Player player) {
        try {
            this.board.move(movePiece.piece(), movePiece.coord(), player);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Player player, Action.PlaceIrrigationStick placeIrrigationStick) {
        try {
            player.getVisibleInventory().decrementIrrigation();
            board.placeIrrigation(placeIrrigationStick.coord(), placeIrrigationStick.side());
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Player player) {
        try {
            gameInventory.decrementIrrigation();
            player.getVisibleInventory().incrementIrrigation();
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private void apply(Player player, Action.UnveilObjective unveilObjective) {
        var visibleInventory = player.getVisibleInventory();
        var privateInventory = player.getPrivateInventory();
        try {
            if (unveilObjective.objective() instanceof HarvestingObjective needs) {
                visibleInventory.useBamboo(Color.GREEN, needs.getGreen());
                visibleInventory.useBamboo(Color.YELLOW, needs.getYellow());
                visibleInventory.useBamboo(Color.PINK, needs.getPink());
            }
            visibleInventory.addObjective(unveilObjective.objective());
            privateInventory.removeObjective(unveilObjective.objective());
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
