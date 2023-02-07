package takenoko.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.action.*;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.EmptyDeckException;
import takenoko.game.tile.TileDeck;
import takenoko.player.InventoryException;
import takenoko.player.Player;
import takenoko.player.PlayerException;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;
    private final Logger out;
    private int numTurn = 1;
    private final GameInventory inventory;
    private final UndoStack state;

    public Game(List<Player> players, Logger out, TileDeck tileDeck, Random random) {
        board = new Board(players);
        this.players = players;
        this.out = out;
        inventory = new GameInventory(20, tileDeck, random);
        this.state = new UndoStack();
        try {
            for (Player player : players) {
                player.getPrivateInventory()
                        .addObjective(inventory.getTilePatternObjectiveDeck().draw());
                player.getPrivateInventory()
                        .addObjective(inventory.getBambooSizeObjectiveDeck().draw());
                player.getPrivateInventory()
                        .addObjective(inventory.getHarvestingObjectiveDeck().draw());
            }
        } catch (EmptyDeckException | InventoryException e) {
            out.log(Level.SEVERE, "Error while initializing the game", e);
        }
    }

    public Optional<Player> play() {
        this.out.log(Level.INFO, "Beginning of the game!");
        // Ideally, we should replace this with a while true, but we can't actually due to the level
        // of our bots.
        while (true) {
            this.out.log(Level.INFO, "Beginning of the tour number " + numTurn + "!");
            playTurn();
            if (endOfGame()) {
                playTurn(); // we need to play a last turn before ending
                return getWinner();
            }
            numTurn++;
        }
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
                    var applier = new ActionApplier(board, out, inventory, player);
                    applier.apply(state, action);
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
            VisibleInventory vi = p.getVisibleInventory();
            this.out.log(Level.INFO, "Player number {0} informations :", numPlayer);
            this.out.log(Level.INFO, "Score : {0}", p.getScore());
            this.out.log(
                    Level.INFO,
                    "Number of objectives achieved : {0}",
                    vi.getFinishedObjectives().size());
            numPlayer++;
        }
    }

    public boolean endOfGame() {
        int objectivesToUnveil =
                switch (players.size()) {
                    case 2 -> 9;
                    case 3 -> 8;
                    case 4 -> 7;
                    default -> throw new IllegalStateException(
                            "Unexpected value: " + players.size());
                };
        for (Player p : players) {
            VisibleInventory vi = p.getVisibleInventory();
            if (objectivesToUnveil == vi.getFinishedObjectives().size()) {
                p.increaseScore(2);
                return true;
            }
        }
        return false;
    }
}
