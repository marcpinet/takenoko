package takenoko;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    private static final int DEFAULT_ACTION_CREDIT = 2;
    private final Board board;
    private final List<Player> players;

    private final Logger out;
    private int numTurn = 1;

    public Game(List<Player> players, Logger out) {
        board = new Board();
        this.players = players;
        this.out = out;
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
                playAction(action);
                numAction++;
            }
            numPlayer++;
            numAction = 1;
        }
        return Optional.of(players.get(0)); // TODO: determine winning condition
    }

    // S1301: we want pattern matching so switch is necessary
    // S1481: pattern matching requires variable name even if unused
    @SuppressWarnings({"java:S1301", "java:S1481"})
    private void playAction(Action action) {
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
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }
}
