package takenoko.game.board;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.tile.Color;
import takenoko.game.tile.PowerUp;
import takenoko.player.InventoryException;

class VisibleInventoryTest {

    VisibleInventory visibleInventory;

    @BeforeEach
    void setUp() {
        visibleInventory = new VisibleInventory();
    }

    @Test
    void testIncrementBamboo() {
        assertEquals(0, visibleInventory.getBamboo(Color.GREEN));
        visibleInventory.incrementBamboo(Color.GREEN);
        assertEquals(1, visibleInventory.getBamboo(Color.GREEN));
    }

    @Test
    void testUseBamboo() throws InventoryException {
        visibleInventory.incrementBamboo(Color.PINK);
        visibleInventory.incrementBamboo(Color.PINK);
        assertEquals(2, visibleInventory.getBamboo(Color.PINK));
        visibleInventory.useBamboo(Color.PINK, 2);
        assertEquals(0, visibleInventory.getBamboo(Color.PINK));
        assertThrows(InventoryException.class, () -> visibleInventory.useBamboo(Color.PINK, 1));
    }

    @Test
    void testHasIrrigation() {
        assertFalse(visibleInventory.hasIrrigation());
        visibleInventory.incrementIrrigation();
        assertTrue(visibleInventory.hasIrrigation());
    }

    @Test
    void testDecrementIrrigation() throws InventoryException {
        visibleInventory.incrementIrrigation();
        assertTrue(visibleInventory.hasIrrigation());
        visibleInventory.decrementIrrigation();
        assertFalse(visibleInventory.hasIrrigation());
        assertThrows(InventoryException.class, () -> visibleInventory.decrementIrrigation());
    }

    @Test
    void testHasPowerUp() {
        assertFalse(visibleInventory.hasPowerUp(PowerUp.WATERSHED));
        visibleInventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(visibleInventory.hasPowerUp(PowerUp.WATERSHED));
    }

    @Test
    void testIncrementPowerUp() {
        assertFalse(visibleInventory.hasPowerUp(PowerUp.WATERSHED));
        visibleInventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(visibleInventory.hasPowerUp(PowerUp.WATERSHED));
    }

    @Test
    void testDecrementPowerUp() throws InventoryException {
        visibleInventory.incrementPowerUp(PowerUp.WATERSHED);
        assertTrue(visibleInventory.hasPowerUp(PowerUp.WATERSHED));
        visibleInventory.decrementPowerUp(PowerUp.WATERSHED);
        assertFalse(visibleInventory.hasPowerUp(PowerUp.WATERSHED));
        assertThrows(
                InventoryException.class,
                () -> visibleInventory.decrementPowerUp(PowerUp.WATERSHED));
    }
}
