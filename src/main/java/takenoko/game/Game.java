package takenoko.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.action.Action;
import takenoko.action.ActionApplier;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.game.objective.Objective;
import takenoko.game.tile.TileDeck;
import takenoko.player.Inventory;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PlayerException;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;
    private final Logger out;
    private final List<Objective> objectives;
    private int numTurn = 1;
    private final GameInventory inventory;
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
        inventory = new GameInventory(20);
    }

    public Optional<Player> play() {
        this.out.log(Level.INFO, "Beginning of the game!");
        while (true) {

            if (numTurn > 200) {
                this.out.log(Level.INFO, "Too many turns, no winner.");
                return Optional.empty();
            }

            this.out.log(Level.INFO, "Beginning of the tour number " + numTurn + "!");
            var winner = playTurn();
            numTurn++;
            if (winner.isPresent()) {
                this.out.log(Level.INFO, "Someone won!");
                this.out.log(Level.INFO, "End of the game.");
                return winner;
            }
        }
    }

    private Optional<Player> playTurn() {
        int numPlayer = 1;
        int numAction = 1;
        for (Player player : players) {
            this.out.log(Level.INFO, "Turn of player number {0} to play!", numPlayer);
            player.beginTurn(DEFAULT_ACTION_CREDIT);
            ArrayList<Action> alreadyPlayedActions = new ArrayList<>();
            while (true) {
                this.out.log(Level.INFO, "Action number {0}:", numAction);
                try {
                    var validator =
                            new ActionValidator(
                                    board,
                                    tileDeck,
                                    inventory,
                                    player.getInventory(),
                                    alreadyPlayedActions);
                    var action = player.chooseAction(board, validator);
                    this.out.log(Level.INFO, "Action: {0}", action);
                    if (!validator.isValid(action)) continue;
                    if (action == Action.END_TURN) break;
                    var applier = new ActionApplier(board, out, inventory, tileDeck);
                    if (applier.apply(action, player)) return Optional.of(player);
                    alreadyPlayedActions.add(action);
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

    private void checkObjectives(Action lastAction, Inventory inventory) {
        for (Objective objective : objectives) {
            objective.isAchieved(board, lastAction, inventory);
        }
    }
}
