package takenoko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}
