package takenoko;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoordTest {

    Coord coordx1y2;

    @BeforeEach
    void setUp() {
        coordx1y2 = new Coord(1, 2);
    }

    @Test
    void componentTest() {
        assertEquals(1, coordx1y2.x());
        assertEquals(2, coordx1y2.y());
        assertEquals(-3, coordx1y2.z());

        assertNotEquals(2, coordx1y2.x());
        assertNotEquals(1, coordx1y2.y());
        assertNotEquals(-2, coordx1y2.z());
    }

    @Test
    void equalsTest() {
        assertEquals(new Coord(1, 2), coordx1y2);
        assertNotEquals(new Coord(2, 2), coordx1y2);
    }

    @Test
    void adjacentCoordsTest() {
        Coord[] adjacentCoords = coordx1y2.adjacentCoords();
        assertEquals(6, adjacentCoords.length);
        assertEquals(new Coord(1, 3), adjacentCoords[0]);
        assertEquals(new Coord(2, 2), adjacentCoords[1]);
        assertEquals(new Coord(2, 1), adjacentCoords[2]);
        assertEquals(new Coord(1, 1), adjacentCoords[3]);
        assertEquals(new Coord(0, 2), adjacentCoords[4]);
        assertEquals(new Coord(0, 3), adjacentCoords[5]);
    }

    @Test
    void isAdjacentToTest() {
        assertTrue(coordx1y2.isAdjacentTo(new Coord(1, 3)));
        assertFalse(coordx1y2.isAdjacentTo(new Coord(2, 5)));
    }
}
