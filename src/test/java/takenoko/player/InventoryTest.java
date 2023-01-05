package takenoko.player;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.objective.BambooSizeObjective;
import takenoko.game.tile.BambooSizeException;
import takenoko.game.tile.Color;
import takenoko.game.tile.PowerUp;

class InventoryTest {

    Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testIncrementBamboo() {
        assertEquals(0, inventory.getBamboo(Color.GREEN));
        inventory.incrementBamboo(Color.GREEN);
        assertEquals(1, inventory.getBamboo(Color.GREEN));
    }

    @Test
    void testUseBamboo() throws InventoryException {
        inventory.incrementBamboo(Color.PINK);
        inventory.incrementBamboo(Color.PINK);
        assertEquals(2, inventory.getBamboo(Color.PINK));
        inventory.useBamboo(Color.PINK, 2);
        assertEquals(0, inventory.getBamboo(Color.PINK));
        assertThrows(InventoryException.class, () -> inventory.useBamboo(Color.PINK, 1));
    }

    @Test
    void testHasIrrigation() {
        assertFalse(inventory.hasIrrigation());
        inventory.incrementIrrigation();
        assertTrue(inventory.hasIrrigation());
    }

    @Test
    void testDecrementIrrigation() throws InventoryException {
        inventory.incrementIrrigation();
        assertTrue(inventory.hasIrrigation());
        inventory.decrementIrrigation();
        assertFalse(inventory.hasIrrigation());
        assertThrows(InventoryException.class, () -> inventory.decrementIrrigation());
    }

    @Test
    void testHasPowerUp() {
        assertFalse(inventory.hasPowerUp(PowerUp.WATERSHED));
        inventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(inventory.hasPowerUp(PowerUp.WATERSHED));
    }

    @Test
    void testIncrementPowerUp() {
        assertFalse(inventory.hasPowerUp(PowerUp.WATERSHED));
        inventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(inventory.hasPowerUp(PowerUp.WATERSHED));
    }

    @Test
    void testDecrementPowerUp() throws InventoryException {
        inventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(inventory.hasPowerUp(PowerUp.WATERSHED));
        inventory.decrementPowerUp(PowerUp.WATERSHED);
        assertFalse(inventory.hasPowerUp(PowerUp.WATERSHED));
        assertThrows(InventoryException.class, () -> inventory.decrementPowerUp(PowerUp.WATERSHED));
    }

    @Test
    void testObjective() throws BambooSizeException, InventoryException {
        var objective = new BambooSizeObjective(2);
        for (int i = 0; i < 5; i++) {
            assertTrue(inventory.canDrawObjective());
            inventory.addObjective(objective);
        }
        assertFalse(inventory.canDrawObjective());
        assertThrows(InventoryException.class, () -> inventory.addObjective(objective));
    }
}
