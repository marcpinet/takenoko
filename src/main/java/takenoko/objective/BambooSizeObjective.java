package takenoko.objective;

import java.util.concurrent.atomic.AtomicBoolean;
import takenoko.Action;
import takenoko.BambooSizeException;
import takenoko.BambooTile;
import takenoko.Board;

public class BambooSizeObjective implements Objective {

    private final int sizeObjective;
    private boolean achieved = false;

    public BambooSizeObjective(int b) throws BambooSizeException {
        if (b < 1 || b > 4) {
            throw new BambooSizeException("Error : unreachable bamboo size.");
        }
        this.sizeObjective = b;
    }

    @Override
    public boolean isAchieved(Board board, Action lastAction) {
        AtomicBoolean res = new AtomicBoolean(false);
        board.applyOnEachTile(
                tile -> {
                    if (!(tile instanceof BambooTile bambooTile)) {
                        return null;
                    }
                    if (bambooTile.getBambooSize() == sizeObjective) {
                        res.set(true);
                    }
                    return null;
                });
        achieved = res.get();
        return achieved;
    }

    @Override
    public boolean wasAchievedAfterLastCheck() {
        return achieved;
    }
}
