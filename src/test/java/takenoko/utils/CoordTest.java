package takenoko.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.tile.TileSide;

class CoordTest {
    Coord center;

    Coord coordx1y2;
    Coord coordx2y2;
    Coord coordx3y2;

    @BeforeEach
    void setUp() {
        center = new Coord(0, 0);
        coordx1y2 = new Coord(1, 2);
        coordx2y2 = new Coord(2, 2);
        coordx3y2 = new Coord(3, 2);
    }

    @Test
    void component() {
        assertEquals(1, coordx1y2.x());
        assertEquals(2, coordx1y2.y());
        assertEquals(-3, coordx1y2.z());

        assertNotEquals(2, coordx1y2.x());
        assertNotEquals(1, coordx1y2.y());
        assertNotEquals(-2, coordx1y2.z());
    }

    @Test
    void equals() {
        assertEquals(new Coord(1, 2), coordx1y2);
        assertNotEquals(coordx2y2, coordx1y2);
    }

    @Test
    void adjacentCoords() {
        Coord[] adjacentCoords = center.adjacentCoords();
        assertEquals(6, adjacentCoords.length);
        assertEquals(new Coord(0, -1), adjacentCoords[0]);
        assertEquals(new Coord(1, -1), adjacentCoords[1]);
        assertEquals(new Coord(1, 0), adjacentCoords[2]);
        assertEquals(new Coord(0, 1), adjacentCoords[3]);
        assertEquals(new Coord(-1, 1), adjacentCoords[4]);
        assertEquals(new Coord(-1, 0), adjacentCoords[5]);
    }

    @Test
    void isAdjacentTo() {
        assertTrue(coordx1y2.isAdjacentTo(coordx2y2));
        assertFalse(coordx1y2.isAdjacentTo(coordx3y2));
    }

    @Test
    void rotate() {
        assertEquals(new Coord(3, -1), coordx1y2.rotate(center));
        assertEquals(new Coord(4, -2), coordx2y2.rotate(center));
        assertEquals(new Coord(5, -3), coordx3y2.rotate(center));
    }

    @Test
    void adjacentCoordSide() {
        assertEquals(coordx2y2, coordx1y2.adjacentCoordSide(TileSide.DOWN_RIGHT));
    }

    @Test
    void isAlignedWith() {
        Coord coordx0y0 = new Coord(0, 0);
        Coord coordx3y0 = new Coord(3, 0);

        assertTrue(coordx0y0.isAlignedWith(coordx3y0));

        Coord coordx0y1 = new Coord(0, 1);
        Coord coordx3y1 = new Coord(3, 1);

        assertTrue(coordx0y1.isAlignedWith(coordx3y1));

        Coord coordx0y2 = new Coord(0, 2);

        assertFalse(coordx0y2.isAlignedWith(coordx3y1));

        Coord coordx1y1 = new Coord(1, 1);

        assertFalse(coordx0y0.isAlignedWith(coordx1y1));
    }
}
