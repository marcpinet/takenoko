package takenoko.game;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.Objective;
import takenoko.game.tile.Color;
import takenoko.game.tile.IrrigationException;
import takenoko.game.tile.TileDeck;
import takenoko.game.tile.TileSide;
import takenoko.player.Inventory;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PlayerException;
import takenoko.utils.Coord;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;
    private final Logger out;
    private final List<Objective> objectives;
    private int numTurn = 1;
    private int irrigationStickLeft = 20;
    private final TileDeck tileDeck;

    public Game(List<Player> players, List<Objective> objectives, Logger out, TileDeck tileDeck) {
        board = new Board();
        this.players = players;
        this.objectives = objectives;
        this.out = out;
        this.tileDeck = tileDeck;
        for (var player : players) {
            // TODO: change how objectives are assigned
            try {
                player.getInventory().addObjective(objectives.get(0));
            } catch (InventoryException e) {
                out.log(Level.SEVERE, "Failed to add objective to player", e);
            }
        }
    }

    public Player play() throws TooManyTurnsException {
        this.out.log(Level.INFO, "Beginning of the game!");
        while (true) {
            if (numTurn > 100) {
                throw new TooManyTurnsException("ERROR : too many turns. Game end.");
            }
            this.out.log(Level.INFO, "Beginning of the tour number " + numTurn + "!");
            var winner = playTurn();
            numTurn++;
            if (winner.isPresent()) {
                this.out.log(Level.INFO, "Someone won!");
                this.out.log(Level.INFO, "End of the game.");
                return winner.get();
            }
        }
    }

    private Optional<Player> playTurn() {
        int numPlayer = 1;
        int numAction = 1;
        for (Player player : players) {
            this.out.log(Level.INFO, "Turn of player number {0} to play!", numPlayer);
            player.beginTurn(DEFAULT_ACTION_CREDIT);
            while (true) {
                this.out.log(Level.INFO, "Action number {0}:", numAction);
                try {
                    var validator =
                            new ActionValidator(
                                    board, tileDeck, irrigationStickLeft, player.getInventory());
                    var action = player.chooseAction(board, validator);
                    if (!validator.isValid(action)) continue;
                    this.out.log(Level.INFO, "Action: {0}", action);
                    if (action == Action.END_TURN) break;
                    if (playAction(action, player)) return Optional.of(player);
                    checkObjectives(action, player.getInventory());
                } catch (PlayerException e) {
                    this.out.log(Level.SEVERE, "Player exception: {0}", e.getMessage());
                }
                numAction++;
            }
            numPlayer++;
            numAction = 1;
        }
        return Optional.empty();
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    // S131: we're using pattern matching, so we don't need a default branch
    @SuppressWarnings({"java:S1301", "java:S1481", "java:S131"})
    private boolean playAction(Action action, Player player) {
        switch (action) {
            case Action.None ignored -> {
                // do nothing
            }
            case Action.EndTurn ignored -> {
                // do nothing
            }
            case Action.PlaceTile placeTile -> {
                try {
                    var tile = tileDeck.draw(placeTile.drawTilePredicate());
                    board.placeTile(placeTile.coord(), tile);
                } catch (Exception e) {
                    this.out.log(Level.SEVERE, e.getMessage());
                }
            }
            case Action.UnveilObjective unveilObjective -> {
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
            case Action.TakeIrrigationStick takeIrrigationStick -> {
                try {
                    takeIrrigationStick(player);
                } catch (Exception e) {
                    this.out.log(Level.SEVERE, e.getMessage());
                }
            }
            case Action.PlaceIrrigationStick placeIrrigationStick -> {
                try {
                    placeIrrigationStick(
                            player, placeIrrigationStick.coord(), placeIrrigationStick.side());
                } catch (Exception e) {
                    this.out.log(Level.SEVERE, e.getMessage());
                }
            }

            case Action.MoveGardener moveGardener -> {
                try {
                    this.board.move(MovablePiece.GARDENER, moveGardener.coord());
                } catch (Exception e) {
                    this.out.log(Level.SEVERE, e.getMessage());
                }
            }

            case Action.MovePanda movePanda -> {
                try {
                    this.board.move(MovablePiece.PANDA, movePanda.coord());
                } catch (Exception e) {
                    this.out.log(Level.SEVERE, e.getMessage());
                }
            }
        }
        return false;
    }

    // take an irrigation stick from the stack and put it in the player's inventory
    private void takeIrrigationStick(Player player) throws BoardException {
        if (irrigationStickLeft == 0) {
            throw new BoardException("No more irrigation stick left");
        }
        player.getInventory().incrementIrrigation();
        irrigationStickLeft--;
    }

    private void placeIrrigationStick(Player player, Coord coord, TileSide side)
            throws IrrigationException, InventoryException {
        player.getInventory().decrementIrrigation();
        board.placeIrrigation(coord, side);
    }

    private void checkObjectives(Action lastAction, Inventory inventory) {
        for (Objective objective : objectives) {
            objective.isAchieved(board, lastAction, inventory);
        }
    }
}
