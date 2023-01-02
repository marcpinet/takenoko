package takenoko.player;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.tile.Color;
import takenoko.game.tile.PowerUp;

public class InventoryTest {

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
    void testUseBamboo() {
        inventory.incrementBamboo(Color.PINK);
        inventory.incrementBamboo(Color.PINK);
        inventory.incrementBamboo(Color.PINK);
        assertEquals(3, inventory.getBamboo(Color.PINK));
        inventory.useBamboo(Color.PINK, 2);
        assertEquals(1, inventory.getBamboo(Color.PINK));
        assertThrows(InventoryException.class, () -> inventory.useBamboo(Color.PINK, 2));
    }

    @Test
    void testHasIrrigation() {
        assertFalse(inventory.hasIrrigation());
        inventory.incrementIrrigation();
        assertTrue(inventory.hasIrrigation());
    }

    @Test
    void testIncrementIrrigation() {
        assertEquals(0, inventory.getIrrigations());
        inventory.incrementIrrigation();
        assertEquals(1, inventory.getIrrigations());
    }

    @Test
    void testDecrementIrrigation() throws InventoryException {
        inventory.incrementIrrigation();
        assertEquals(1, inventory.getIrrigations());
        inventory.decrementIrrigation();
        assertEquals(0, inventory.getIrrigations());
        assertThrows(InventoryException.class, () -> inventory.decrementIrrigation());
    }

    @Test
    void testHasPowerUp() {
        assertFalse(inventory.hasPowerUp(PowerUp.WATERSHED));
        inventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(inventory.hasPowerUp(PowerUp.WATERSHED));
    }
}
