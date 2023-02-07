package takenoko.objective;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.objective.Objective;
import takenoko.game.tile.Color;

class HarvestingObjectiveTest {

    HarvestingObjective h1, h2, h3;
    VisibleInventory visibleInventory;

    @BeforeEach
    void setUp() {
        h1 = new HarvestingObjective(1, 1, 1);
        h2 = new HarvestingObjective(2, 2, 0);
        h3 = new HarvestingObjective(3, 0, 0);
        visibleInventory = new VisibleInventory();
    }

    @Test
    void testStatus() {

        assertEquals(new Objective.Status(0, 3), h1.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(0, 4), h2.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(0, 3), h3.computeAchieved(null, null, visibleInventory));

        for (int i = 0; i < 3; i++) {
            visibleInventory.incrementBamboo(Color.GREEN);
        }

        assertEquals(new Objective.Status(1, 3), h1.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(2, 4), h2.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(3, 3), h3.computeAchieved(null, null, visibleInventory));

        for (int i = 0; i < 2; i++) {
            visibleInventory.incrementBamboo(Color.YELLOW);
        }

        assertEquals(new Objective.Status(2, 3), h1.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(4, 4), h2.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(3, 3), h3.computeAchieved(null, null, visibleInventory));

        visibleInventory.incrementBamboo(Color.PINK);

        assertEquals(new Objective.Status(3, 3), h1.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(4, 4), h2.computeAchieved(null, null, visibleInventory));
        assertEquals(new Objective.Status(3, 3), h3.computeAchieved(null, null, visibleInventory));
    }
}
