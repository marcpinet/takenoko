package takenoko.objective;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.tile.Color;

class HarvestingObjectiveTest {

    HarvestingObjective h1, h2, h3, h4;
    VisibleInventory visibleInventory;

    @BeforeEach
    void setUp() {
        h1 = new HarvestingObjective(1, 1, 1);
        h2 = new HarvestingObjective(2, 2, 0);
        h3 = new HarvestingObjective(3, 0, 0);
        visibleInventory = new VisibleInventory();
    }

    @Test
    void testIsAchieved() {
        assertFalse(h1.isAchieved(null, null, visibleInventory));
        assertFalse(h2.isAchieved(null, null, visibleInventory));
        assertFalse(h3.isAchieved(null, null, visibleInventory));

        for (int i = 0; i < 3; i++) {
            visibleInventory.incrementBamboo(Color.GREEN);
        }

        assertFalse(h1.isAchieved(null, null, visibleInventory));
        assertFalse(h2.isAchieved(null, null, visibleInventory));
        assertTrue(h3.isAchieved(null, null, visibleInventory));

        for (int i = 0; i < 2; i++) {
            visibleInventory.incrementBamboo(Color.YELLOW);
        }

        assertFalse(h1.isAchieved(null, null, visibleInventory));
        assertTrue(h2.isAchieved(null, null, visibleInventory));
        assertTrue(h3.isAchieved(null, null, visibleInventory));

        visibleInventory.incrementBamboo(Color.PINK);

        assertTrue(h1.isAchieved(null, null, visibleInventory));
        assertTrue(h2.isAchieved(null, null, visibleInventory));
        assertTrue(h3.isAchieved(null, null, visibleInventory));
    }
}
