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
import takenoko.game.objective.Objective;
import takenoko.game.tile.EmptyDeckException;
import takenoko.game.tile.TileDeck;
import takenoko.player.InventoryException;
import takenoko.player.Player;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;
    private final Logger out;
    private final GameInventory inventory;
    private final UndoStack state;
    private int numTurn = 1;

    public Game(
            List<Player> players, Logger out, TileDeck tileDeck, WeatherDice dice, Random random) {
        board = new Board(players);
        this.players = players;
        this.out = out;
        inventory = new GameInventory(20, tileDeck, random, dice);
        this.state = new UndoStack();
        try {
            for (Player player : players) {
                for (var objectiveType : Objective.Type.values()) {
                    player.getPrivateInventory()
                            .addObjective(inventory.getObjectiveDeck(objectiveType).draw());
                }
            }
        } catch (EmptyDeckException | InventoryException e) {
            out.log(Level.SEVERE, "Error while initializing the game", e);
        }
    }

    public Optional<Player> play(int maxTurns) {
        this.out.log(Level.INFO, "Beginning of the game!\n\n");
        while (numTurn <= maxTurns) {
            this.out.log(Level.INFO, "Beginning of the turn number {0}!\n", numTurn);
            playTurn();
            if (endOfGame()) {
                playTurn(); // we need to play a last turn before ending
                return getWinner();
            }
            numTurn++;
        }
        return Optional.empty();
    }

    public Optional<Player> play() {
        return play(1000);
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

    public int getNumTurn() {
        return numTurn;
    }

    private void playTurn() {
        int numPlayer = 1;
        int numAction = 1;
        for (Player player : players) {
            this.out.log(Level.INFO, "Turn of player number {0} to play!\n", numPlayer);

            ArrayList<Action> alreadyPlayedActions = new ArrayList<>();

            var weather = pickWeather(player);

            var actionCredit = DEFAULT_ACTION_CREDIT;
            if (weather == WeatherDice.Face.SUN) {
                actionCredit++;
            }
            player.beginTurn(actionCredit);

            while (!playAction(numAction, player, alreadyPlayedActions, weather)) {
                numAction++;
            }

            numPlayer++;
            numAction = 1;
        }
        displayInventories();
    }

    private boolean playAction(
            int numAction,
            Player player,
            ArrayList<Action> alreadyPlayedActions,
            WeatherDice.Face weather) {
        this.out.log(Level.INFO, "Action number {0}:", numAction);
        var actionLister = makeActionLister(player, alreadyPlayedActions, weather);
        var action = player.chooseAction(board, actionLister);
        this.out.log(Level.INFO, "Action: {0}\n", action);
        if (action == Action.END_TURN) return true;
        var applier = new ActionApplier(board, out, inventory, player);
        applier.apply(state, action);
        alreadyPlayedActions.add(action);
        checkObjectives(action);
        return false;
    }

    private WeatherDice.Face pickWeather(Player player) {
        WeatherDice.Face weather = inventory.getWeatherDice().throwDice();

        if (weather == WeatherDice.Face.ANY) {
            weather =
                    player.chooseWeather(
                            List.of(
                                    WeatherDice.Face.SUN,
                                    WeatherDice.Face.CLOUDY,
                                    WeatherDice.Face.RAIN,
                                    WeatherDice.Face.WIND,
                                    WeatherDice.Face.THUNDERSTORM));
        }
        if (weather == WeatherDice.Face.CLOUDY && inventory.getPowerUpReserve().isEmpty()) {
            weather =
                    player.chooseWeather(
                            List.of(
                                    WeatherDice.Face.SUN,
                                    WeatherDice.Face.RAIN,
                                    WeatherDice.Face.WIND,
                                    WeatherDice.Face.THUNDERSTORM));
        }
        this.out.log(Level.INFO, "Weather: {0}", weather);
        return weather;
    }

    private PossibleActionLister makeActionLister(
            Player player, List<Action> alreadyPlayedActions, WeatherDice.Face weather) {
        var validator =
                new ActionValidator(board, inventory, player, weather, alreadyPlayedActions);

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
            this.out.log(Level.INFO, "Player number {0} information :", numPlayer);
            this.out.log(Level.INFO, "Score : {0}", p.getScore());
            this.out.log(
                    Level.INFO,
                    "Number of objectives achieved : {0}\n\n",
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
