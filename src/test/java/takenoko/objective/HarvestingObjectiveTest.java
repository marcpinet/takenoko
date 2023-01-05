package takenoko.objective;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.objective.HarvestingObjective;
import takenoko.game.tile.Color;
import takenoko.player.Inventory;

class HarvestingObjectiveTest {

    HarvestingObjective h1, h2, h3, h4;
    Inventory inventory;

    @BeforeEach
    void setUp() {
        h1 = new HarvestingObjective(1, 1, 1);
        h2 = new HarvestingObjective(2, 2, 0);
        h3 = new HarvestingObjective(3, 0, 0);
        inventory = new Inventory();
    }

    @Test
    void testIsAchieved() {
        assertFalse(h1.isAchieved(null, null, inventory));
        assertFalse(h2.isAchieved(null, null, inventory));
        assertFalse(h3.isAchieved(null, null, inventory));

        for (int i = 0; i < 3; i++) {
            inventory.incrementBamboo(Color.GREEN);
        }

        assertFalse(h1.isAchieved(null, null, inventory));
        assertFalse(h2.isAchieved(null, null, inventory));
        assertTrue(h3.isAchieved(null, null, inventory));

        for (int i = 0; i < 2; i++) {
            inventory.incrementBamboo(Color.YELLOW);
        }

        assertFalse(h1.isAchieved(null, null, inventory));
        assertTrue(h2.isAchieved(null, null, inventory));
        assertTrue(h3.isAchieved(null, null, inventory));

        inventory.incrementBamboo(Color.PINK);

        assertTrue(h1.isAchieved(null, null, inventory));
        assertTrue(h2.isAchieved(null, null, inventory));
        assertTrue(h3.isAchieved(null, null, inventory));
    }
}
