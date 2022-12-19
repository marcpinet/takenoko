package takenoko;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.objective.Objective;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;
    private final Logger out;
    private final List<Objective> objectives;
    private int numTurn = 1;
    private int irrigationSticksLeft = 20;

    public Game(List<Player> players, List<Objective> objectives, Logger out) {
        board = new Board();
        this.players = players;
        this.objectives = objectives;
        this.out = out;
        for (var player : players) {
            // TODO: change how objectives are assigned
            player.addObjective(objectives.get(0));
        }
    }

    public Player play() {
        this.out.log(Level.INFO, "Beginning of the game!");
        while (true) {
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
            this.out.log(Level.INFO, "Turn of player number " + numPlayer + " to play!");
            player.beginTurn(DEFAULT_ACTION_CREDIT);
            while (!player.wantsToEndTurn()) {
                this.out.log(
                        Level.INFO,
                        "Player number " + numPlayer + " do his action number " + numAction + ":");
                var action = player.chooseAction(board);
                this.out.log(Level.INFO, "Action: " + action);
                if (playAction(action)) return Optional.of(player);
                checkObjectives(action);
                numAction++;
            }
            growBamboosOnBambooTiles();
            numPlayer++;
            numAction = 1;
        }
        return Optional.empty();
    }

    private void growBamboosOnBambooTiles() {
        board.applyOnEachTile(
                tile -> {
                    if (tile instanceof BambooTile bambooTile && bambooTile.isCultivable()) {
                        try {
                            bambooTile.growBamboo();
                        } catch (BambooSizeException ignored) {
                            this.out.log(Level.WARNING, "Bamboo size exception ignored");
                        }
                    }
                    return null;
                });
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    // S131: we're using pattern matching, so we don't need a default branch
    @SuppressWarnings({"java:S1301", "java:S1481", "java:S131"})
    private boolean playAction(Action action) {
        switch (action) {
            case Action.None ignored -> {
                // do nothing
            }
            case Action.PlaceTile placeTile -> {
                try {
                    board.placeTile(placeTile.coord(), placeTile.tile());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case Action.UnveilObjective ignored -> {
                return true;
            }
        }
        return false;
    }

    private void checkObjectives(Action lastAction) {
        for (Objective objective : objectives) {
            objective.isAchieved(board, lastAction);
        }
    }
}
