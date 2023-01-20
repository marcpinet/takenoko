package takenoko.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.action.Action;
import takenoko.action.ActionApplier;
import takenoko.action.ActionValidator;
import takenoko.action.PossibleActionLister;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.Objective;
import takenoko.game.tile.TileDeck;
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

    public Game(List<Player> players, List<Objective> objectives, Logger out, TileDeck tileDeck) {
        board = new Board();
        this.players = players;
        this.objectives = objectives;
        this.out = out;
        inventory = new GameInventory(20, tileDeck);
    }

    public Optional<Player> play() {
        this.out.log(Level.INFO, "Beginning of the game!");
        while (numTurn < 200) {
            this.out.log(Level.INFO, "Beginning of the tour number " + numTurn + "!");
            playTurn();
            numTurn++;
        }
        return getWinner();
    }

    private Optional<Player> getWinner() {
        Optional<Player> winner = Optional.empty();
        for (var player : players) {
            if (player.getScore() > winner.map(Player::getScore).orElse(0)) {
                winner = Optional.of(player);
            }
        }
        return winner;
    }

    private void playTurn() {
        int numPlayer = 1;
        int numAction = 1;
        for (Player player : players) {
            this.out.log(Level.INFO, "Turn of player number {0} to play!", numPlayer);
            player.beginTurn(DEFAULT_ACTION_CREDIT);
            ArrayList<Action> alreadyPlayedActions = new ArrayList<>();
            while (true) {
                this.out.log(Level.INFO, "Action number {0}:", numAction);
                try {
                    var actionLister = makeActionLister(player, alreadyPlayedActions);
                    var action = player.chooseAction(board, actionLister);
                    this.out.log(Level.INFO, "Action: {0}", action);
                    if (action == Action.END_TURN) break;
                    var applier =
                            new ActionApplier(board, out, inventory, player.getPrivateInventory());
                    applier.apply(action, player);
                    alreadyPlayedActions.add(action);
                    checkObjectives(action, player.getVisibleInventory());
                } catch (PlayerException e) {
                    this.out.log(Level.SEVERE, "Player exception: {0}", e.getMessage());
                }
                numAction++;
            }
            numPlayer++;
            numAction = 1;
        }
    }

    private PossibleActionLister makeActionLister(
            Player player, List<Action> alreadyPlayedActions) {
        var validator =
                new ActionValidator(
                        board,
                        inventory,
                        player.getPrivateInventory(),
                        player.getVisibleInventory(),
                        alreadyPlayedActions);

        return new PossibleActionLister(board, validator, player.getPrivateInventory());
    }

    private void checkObjectives(Action lastAction, VisibleInventory visibleInventory) {
        for (Objective objective : objectives) {
            objective.isAchieved(board, lastAction, visibleInventory);
        }
    }
}
