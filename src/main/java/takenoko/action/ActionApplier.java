package takenoko.action;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import takenoko.game.GameInventory;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.tile.*;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.utils.Pair;

public class ActionApplier {
    private final Board board;
    private final Logger out;
    private final GameInventory gameInventory;
    private final Player player;

    public ActionApplier(Board board, Logger out, GameInventory gameInventory, Player player) {
        this.board = board;
        this.out = out;
        this.gameInventory = gameInventory;
        this.player = player;
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    // S131: we're using pattern matching, so we don't need a default branch
    @SuppressWarnings({"java:S1301", "java:S1481", "java:S131"})
    public void apply(UndoStack undoStack, Action action) {
        var undo =
                switch (action) {
                    case Action.None ignored -> UndoAction.NONE;
                    case Action.EndTurn ignored -> UndoAction.END_TURN;
                    case Action.PlaceTile placeTile -> apply(placeTile);
                    case Action.UnveilObjective unveilObjective -> apply(player, unveilObjective);
                    case Action.TakeIrrigationStick takeIrrigationStick -> apply(
                            takeIrrigationStick, player);
                    case Action.PlaceIrrigationStick placeIrrigationStick -> apply(
                            player, placeIrrigationStick);
                    case Action.MovePiece movePiece -> apply(movePiece);
                    case Action.BeginSimulation ignored -> UndoAction.BEGIN_SIMULATION;
                    case Action.EndSimulation ignored -> {
                        undoUntilSimulationStart(undoStack);
                        yield UndoAction.NONE;
                    }
                    case Action.SimulateActions simulateAction -> apply(undoStack, simulateAction);
                    case Action.TakeObjective takeObjective -> apply(takeObjective);
                    case Action.PickPowerUp pickPowerUp -> apply(pickPowerUp);
                    case Action.PlacePowerUp placePowerUp -> apply(placePowerUp);
                    case Action.GrowOneTile growOneTile -> apply(growOneTile);
                    case Action.MovePandaAnywhere movePandaAnywhere -> apply(movePandaAnywhere);
                };

        if (undo == UndoAction.END_TURN) {
            undoStack.clear();
        } else {
            undoStack.push(undo);
        }
    }

    @SuppressWarnings("SameReturnValue")
    private UndoAction apply(UndoStack undoStack, Action.SimulateActions simulateActions) {
        for (var action : simulateActions.alternativeActions()) {
            apply(undoStack, Action.BEGIN_SIMULATION);

            apply(undoStack, action);

            var newObjectiveStatuses =
                    player.getPrivateInventory().getObjectives().stream()
                            .map(
                                    objective ->
                                            Pair.of(
                                                    objective,
                                                    objective.computeAchieved(
                                                            board,
                                                            action,
                                                            player.getVisibleInventory())))
                            .collect(Collectors.toMap(Pair::first, Pair::second));

            simulateActions
                    .outObjectiveStatus()
                    .put(action, new LinkedHashMap<>(newObjectiveStatuses));

            apply(undoStack, Action.END_SIMULATION);
        }

        return UndoAction.NONE;
    }

    private void undoUntilSimulationStart(UndoStack undoStack) {
        while (true) {
            var action = undoStack.pop();
            if (action == UndoAction.BEGIN_SIMULATION) break;
            undo(action);
        }
        for (var objective : player.getPrivateInventory().getObjectives()) {
            objective.forceRecomputeOnNextCheck();
        }
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    // S131: we're using pattern matching, so we don't need a default branch
    @SuppressWarnings({"java:S1301", "java:S1481", "java:S131", "DuplicateBranchesInSwitch"})
    private void undo(UndoAction action) {
        switch (action) {
            case UndoAction.None ignored -> {}
            case UndoAction.EndTurn ignored -> {}
            case UndoAction.PlaceTile placeTile -> undo(placeTile);
            case UndoAction.UnveilObjective unveilObjective -> undo(player, unveilObjective);
            case UndoAction.TakeIrrigationStick takeIrrigationStick -> undo(
                    takeIrrigationStick, player);
            case UndoAction.PlaceIrrigationStick placeIrrigationStick -> undo(
                    player, placeIrrigationStick);
            case UndoAction.MovePiece movePiece -> undo(movePiece);
            case UndoAction.BeginSimulation ignored -> {
                // do nothing
            }
            case UndoAction.TakeObjective takeObjective -> undo(takeObjective);
            case UndoAction.PickPowerUp pickPowerUp -> undo(pickPowerUp);
            case UndoAction.PlacePowerUp placePowerUp -> undo(placePowerUp);
            case UndoAction.GrowOneTile growOneTile -> undo(growOneTile);
        }
    }

    private UndoAction apply(Action.MovePandaAnywhere movePandaAnywhere) {
        try {
            var boardCopy = new Board(board);
            var inventoryCopy = new VisibleInventory(player.getVisibleInventory());
            board.move(MovablePiece.PANDA, movePandaAnywhere.to(), player, true);
            return new UndoAction.MovePiece(boardCopy, inventoryCopy);
        } catch (BoardException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private UndoAction apply(Action.GrowOneTile growOneTile) {
        try {
            var bambooTile = (BambooTile) board.getTile(growOneTile.at());
            bambooTile.growBamboo();
            return new UndoAction.GrowOneTile(growOneTile.at());
        } catch (BoardException | BambooSizeException | BambooIrrigationException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private void undo(UndoAction.GrowOneTile growOneTile) {
        try {
            var bambooTile = (BambooTile) board.getTile(growOneTile.at());
            bambooTile.shrinkBamboo();
        } catch (BoardException | BambooSizeException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private UndoAction apply(Action.PlacePowerUp placePowerUp) {
        try {
            player.getVisibleInventory().decrementPowerUp(placePowerUp.powerUp());

            var tile = (BambooTile) board.getTile(placePowerUp.coord());
            tile.setPowerUp(placePowerUp.powerUp());

            return new UndoAction.PlacePowerUp(placePowerUp.coord(), placePowerUp.powerUp());

        } catch (PowerUpException | InventoryException | BoardException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    public void undo(UndoAction.PlacePowerUp placePowerUp) {
        try {
            player.getVisibleInventory().incrementPowerUp(placePowerUp.powerUp());

            var tile = (BambooTile) board.getTile(placePowerUp.coord());
            tile.setPowerUp(PowerUp.NONE);

        } catch (PowerUpException | BoardException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private UndoAction apply(Action.PickPowerUp pickPowerUp) {
        try {
            player.getVisibleInventory().incrementPowerUp(pickPowerUp.powerUp());
            gameInventory.getPowerUpReserve().pick(pickPowerUp.powerUp());

            return new UndoAction.PickPowerUp(pickPowerUp.powerUp());
        } catch (PowerUpNotAvailableException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private void undo(UndoAction.PickPowerUp pickPowerUp) {
        try {
            player.getVisibleInventory().decrementPowerUp(pickPowerUp.powerUp());
            gameInventory.getPowerUpReserve().putBack(pickPowerUp.powerUp());
        } catch (InventoryException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private UndoAction apply(Action.MovePiece movePiece) {
        try {
            var boardCopy = new Board(board);
            var visibleInventoryCopy = new VisibleInventory(player.getVisibleInventory());
            this.board.move(movePiece.piece(), movePiece.to(), player);
            return new UndoAction.MovePiece(boardCopy, visibleInventoryCopy);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    // FIXME

    private void undo(UndoAction.MovePiece movePiece) {
        try {
            board.restore(movePiece.previousBoard());
            player.getVisibleInventory().restore(movePiece.previousInventory());
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private UndoAction apply(Action.TakeObjective takeObjective) {
        try {
            var deck = gameInventory.getObjectiveDeck(takeObjective.type());
            var objective = deck.draw();
            var inventory = player.getPrivateInventory();
            inventory.addObjective(objective);
            return new UndoAction.TakeObjective(takeObjective.type(), objective);
        } catch (InventoryException | EmptyDeckException e) {
            out.log(Level.SEVERE, "Error while taking objective", e);
        }
        return UndoAction.NONE;
    }

    private void undo(UndoAction.TakeObjective takeObjective) {
        try {
            player.getPrivateInventory().removeObjective(takeObjective.objective());
            var deck = gameInventory.getObjectiveDeck(takeObjective.type());
            deck.addFirst(takeObjective.objective());
        } catch (InventoryException e) {
            this.out.log(Level.SEVERE, "Player inventory is empty", e);
        }
    }

    private UndoAction apply(Player player, Action.PlaceIrrigationStick placeIrrigationStick) {
        try {
            player.getVisibleInventory().decrementIrrigation();
            var boardCopy = new Board(board);
            board.placeIrrigation(placeIrrigationStick.coord(), placeIrrigationStick.side());
            return new UndoAction.PlaceIrrigationStick(boardCopy);
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private void undo(Player player, UndoAction.PlaceIrrigationStick placeIrrigationStick) {
        player.getVisibleInventory().incrementIrrigation();
        board.restore(placeIrrigationStick.previousBoard());
    }

    private UndoAction apply(Action.TakeIrrigationStick ignored, Player player) {
        try {
            gameInventory.decrementIrrigation();
            player.getVisibleInventory().incrementIrrigation();
            return new UndoAction.TakeIrrigationStick();
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private void undo(UndoAction.TakeIrrigationStick ignored, Player player) {
        try {
            gameInventory.incrementIrrigation();
            player.getVisibleInventory().decrementIrrigation();
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }

    private UndoAction apply(Player player, Action.UnveilObjective unveilObjective) {
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
            return new UndoAction.UnveilObjective(unveilObjective.objective());
        } catch (InventoryException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private void undo(Player player, UndoAction.UnveilObjective unveilObjective) {
        var inventory = player.getVisibleInventory();
        if (unveilObjective.objective() instanceof HarvestingObjective needs) {
            for (int i = 0; i < needs.getGreen(); i++) {
                inventory.incrementBamboo(Color.GREEN);
            }
            for (int i = 0; i < needs.getYellow(); i++) {
                inventory.incrementBamboo(Color.YELLOW);
            }
            for (int i = 0; i < needs.getPink(); i++) {
                inventory.incrementBamboo(Color.PINK);
            }
        }
        inventory.removeObjective(unveilObjective.objective());
        try {
            player.getPrivateInventory().addObjective(unveilObjective.objective());
        } catch (InventoryException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        player.decreaseScore(unveilObjective.objective().getScore());
    }

    private UndoAction apply(Action.PlaceTile placeTile) {
        try {
            var tile = gameInventory.getTileDeck().draw(placeTile.drawPredicate());
            board.placeTile(placeTile.coord(), tile);
            return new UndoAction.PlaceTile(placeTile.coord());
        } catch (Exception e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
        return UndoAction.NONE;
    }

    private void undo(UndoAction.PlaceTile placeTile) {
        try {
            var tile = board.getTile(placeTile.coord());
            board.removeTile(placeTile.coord());
            gameInventory.getTileDeck().addFirst(tile);
        } catch (BoardException e) {
            this.out.log(Level.SEVERE, e.getMessage());
        }
    }
}
