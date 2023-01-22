package takenoko.game;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.action.Action;
import takenoko.action.ActionApplier;
import takenoko.action.ActionValidator;
import takenoko.action.PossibleActionLister;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.TileDeck;
import takenoko.player.Player;
import takenoko.player.PlayerException;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;
    private final Logger out;
    private int numTurn = 1;
    private final GameInventory inventory;

    public Game(List<Player> players, Logger out, TileDeck tileDeck) {
        Map<Player, VisibleInventory> playerInventories = new HashMap<>();
        for (Player p : players) {
            playerInventories.put(p, p.getVisibleInventory());
        }
        board = new Board(playerInventories);
        this.players = players;
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
                    checkObjectives(action);
                } catch (PlayerException e) {
                    this.out.log(Level.SEVERE, "Player exception: {0}", e.getMessage());
                }
                numAction++;
            }
            numPlayer++;
            numAction = 1;
        }
        displayInventories();
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

    private void checkObjectives(Action lastAction) {
        for (var player : players) {
            for (var obj : player.getPrivateInventory().getObjectives())
                obj.computeAchieved(board, lastAction, player.getVisibleInventory());
        }
    }

    private void displayInventories() {
        int numPlayer = 1;
        for (Player p : players) {
            this.out.log(Level.INFO, "Player number {0} informations :", numPlayer);
            this.out.log(Level.INFO, "Score : {0}", board.getPlayerScore(p));
            numPlayer++;
        }
    }
}
