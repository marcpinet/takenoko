package takenoko;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    private final Board board;
    private final List<Player> players;

    private final Logger out;

    public Game(List<Player> players, Logger out) {
        board = new Board();
        this.players = players;
        this.out = out;
    }

    public Player play() {
        while (true) {
            var winner = playTurn();
            if (winner.isPresent()) {
                this.out.log(Level.INFO, "Someone won!");
                return winner.get();
            }
        }
    }

    private Optional<Player> playTurn() {
        for (Player player : players) {
            var actions = player.chooseActions(board);
            playAction(actions.first());
            playAction(actions.second());
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
