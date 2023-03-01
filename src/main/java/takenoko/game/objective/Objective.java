package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

/**
 * This interface defines what an objective is in takenoko. There are three kinds of objective :
 *
 * <ul>
 *   <li>BambooSizeObjective, which depends on the color, number and size of bamboos present on a
 *       tile
 *   <li>HarvestingObjective, which depends on the color and the number of bamboos present in the
 *       player's inventory
 *   <li>TilePatternObjective, which depends on the color and the layout of the tiles on the board
 * </ul>
 *
 * The interface is sealed to make sure we can't define an unexisting type of objective.
 *
 * <p>Basic idea : the game will call the method computeAchieved() to compute the status of the
 * objective, after each action. Players can then call the method isAchieved() to know if they have
 * achieved the objective, or `status()` to know how far they are from achieving it.
 */
public sealed interface Objective
        permits BambooSizeObjective, HarvestingObjective, TilePatternObjective {
    enum Type {
        BAMBOO_SIZE,
        HARVESTING,
        TILE_PATTERN
    }

    /**
     * This record is used to show the progression of an objective.
     *
     * @param completed
     * @param totalToComplete
     */
    record Status(int completed, int totalToComplete) {
        public boolean achieved() {
            return completed >= totalToComplete;
        }

        public float progressFraction() {
            return (float) completed / totalToComplete;
        }
    }

    Status computeAchieved(Board board, Action lastAction, VisibleInventory visibleInventory);

    Status status();

    /**
     * Used in the UndoAction to force recomputing the status of the objective on the next call to
     * `computeAchieved()`
     *
     * <p>Indeed, some objectives can cache their status, and the cache is not updated when an
     * action is undone.
     */
    void forceRecomputeOnNextCheck();

    default boolean isAchieved() {
        return status().achieved();
    }

    int getScore();

    Type getType();
}
