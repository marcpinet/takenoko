package takenoko.game.objective;

import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

/**
 *
 * This interface define what an objective is in takenoko. There is three kinds of objective :
 * BambooSizeObjective, which depends on the color, number and size of bamboos present on a tile
 * HarvestingObjective, which depends on the color and the number of bamboos present in the player's inventory
 * TilePatternObjective, which depends on the color and the layout of the tiles on the board
 * The interface is sealed to make sure that we can't define an inexisting type of objective.
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
     * Used in the UndoAction
     */
    void forceRecomputeOnNextCheck();

    default boolean isAchieved() {
        return status().achieved();
    }

    int getScore();

    Type getType();
}
