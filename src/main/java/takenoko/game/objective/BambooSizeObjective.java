package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.*;

public class BambooSizeObjective implements Objective {

    private final int numberOfBamboos;
    private final int sizeObjective;
    private final Color color;
    private final int score;
    private boolean achieved = false;
    private final PowerUpNecessity
            powerUpNecessity; // MANDATORY if the objective need a PowerUp to be completed,
    // FORBIDDEN if PowerUps are forbidden, else NO_MATTER.
    private final PowerUp powerUp;

    public BambooSizeObjective(
            int nbOfBamboos,
            int size,
            Color c,
            int score,
            PowerUpNecessity powerUpNecessity,
            PowerUp powerUp)
            throws BambooSizeException {

        if (nbOfBamboos < 1 || nbOfBamboos > 4) {
            throw new BambooSizeException("Error : unreachable number of bamboos.");
        }
        if (size < 1 || size > 4) {
            throw new BambooSizeException("Error : unreachable bamboo size.");
        }

        this.numberOfBamboos = nbOfBamboos;
        this.sizeObjective = size;
        this.color = c;
        this.score = score;
        this.powerUpNecessity = powerUpNecessity;
        this.powerUp = powerUp;
    }

    public BambooSizeObjective(int nbOfBamboos, int size, Color c) throws BambooSizeException {
        this(nbOfBamboos, size, c, 1, PowerUpNecessity.NO_MATTER, PowerUp.NONE);
    }

    public BambooSizeObjective(int nbOfBamboos, int size, Color c, int score)
            throws BambooSizeException {
        this(nbOfBamboos, size, c, score, PowerUpNecessity.NO_MATTER, PowerUp.NONE);
    }

    @Override
    public boolean computeAchieved(Board board, Action lastAction, VisibleInventory ignored) {
        int nbOfBamboos = numberOfBamboos;
        boolean powerUpCondition = false;
        achieved = false;
        for (var coord : board.getPlacedCoords()) {
            Tile tile = null;
            try {
                tile = board.getTile(coord);
            } catch (BoardException e) {
                throw new IllegalStateException();
            }
            if (tile instanceof BambooTile bambooTile
                    && bambooTile.getBambooSize() == sizeObjective
                    && bambooTile.getColor() == color) {
                nbOfBamboos--;
                powerUpCondition =
                        switch (this.powerUpNecessity) {
                            case FORBIDDEN -> bambooTile.getPowerUp().equals(PowerUp.NONE);
                            case MANDATORY -> bambooTile.getPowerUp().equals(this.powerUp);
                            case NO_MATTER -> true;
                        };
            }
        }
        if (nbOfBamboos <= 0 && powerUpCondition) {
            achieved = true;
        }
        return achieved;
    }

    @Override
    public boolean isAchieved() {
        return achieved;
    }

    @Override
    public int getScore() {
        return score;
    }
}
