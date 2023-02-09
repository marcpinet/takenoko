package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.*;

public final class BambooSizeObjective implements Objective {
    private final int numberOfBamboos;
    private final int sizeObjective;
    private final Color color;
    private final int score;
    // MANDATORY if the objective need a PowerUp to be completed,
    // FORBIDDEN if PowerUps are forbidden, else NO_MATTER.
    private final PowerUpNecessity powerUpNecessity;

    private final PowerUp powerUp;
    private Status status;

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
        resetStatus(0);
    }

    public BambooSizeObjective(int nbOfBamboos, int size, Color c) throws BambooSizeException {
        this(nbOfBamboos, size, c, 1, PowerUpNecessity.NO_MATTER, PowerUp.NONE);
    }

    public BambooSizeObjective(int nbOfBamboos, int size, Color c, int score)
            throws BambooSizeException {
        this(nbOfBamboos, size, c, score, PowerUpNecessity.NO_MATTER, PowerUp.NONE);
    }

    Status resetStatus(int completion) {
        status =
                new Status(
                        completion,
                        numberOfBamboos + (powerUpNecessity == PowerUpNecessity.NO_MATTER ? 0 : 1));
        return status;
    }

    @Override
    public Status computeAchieved(Board board, Action lastAction, VisibleInventory ignored) {
        resetStatus(0);
        int completed = 0;
        for (var coord : board.getPlacedCoords()) {
            Tile tile;
            try {
                tile = board.getTile(coord);
            } catch (BoardException e) {
                throw new IllegalStateException();
            }
            if (tile instanceof BambooTile bambooTile
                    && bambooTile.getBambooSize() == sizeObjective
                    && bambooTile.getColor() == color) {
                completed++;
                completed +=
                        switch (this.powerUpNecessity) {
                            case FORBIDDEN -> bambooTile.getPowerUp().equals(PowerUp.NONE) ? 1 : 0;
                            case MANDATORY -> bambooTile.getPowerUp().equals(this.powerUp) ? 1 : 0;
                            case NO_MATTER -> 0;
                        };
            }
        }
        // prevent >100% progress
        return resetStatus(Math.min(completed, status.totalToComplete()));
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public void forceRecomputeOnNextCheck() {
        // nothing to do
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public Type getType() {
        return Type.BAMBOO_SIZE;
    }
}
