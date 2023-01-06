package takenoko.game.objective;

import java.util.concurrent.atomic.AtomicBoolean;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.tile.BambooSizeException;
import takenoko.game.tile.BambooTile;
import takenoko.player.Inventory;

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
    public boolean isAchieved(Board board, Action lastAction, Inventory ignored) {
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

    @Override
    public int getScore() {
        return 1;
    }
}
