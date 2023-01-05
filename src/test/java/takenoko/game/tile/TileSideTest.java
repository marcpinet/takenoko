package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TileSideTest {

    @Test
    void rightSideTest() {
        assertEquals(TileSide.UP_RIGHT, TileSide.UP.rightSide());
        assertEquals(TileSide.UP_LEFT, TileSide.DOWN_LEFT.rightSide());
    }

    @Test
    void leftSideTest() {
        assertEquals(TileSide.UP_LEFT, TileSide.UP.leftSide());
        assertEquals(TileSide.UP_RIGHT, TileSide.DOWN_RIGHT.leftSide());
    }

    @Test
    void oppositeSideTest() {
        assertEquals(TileSide.UP_LEFT, TileSide.DOWN_RIGHT.oppositeSide());
        assertEquals(TileSide.UP, TileSide.DOWN.oppositeSide());
    }
}
