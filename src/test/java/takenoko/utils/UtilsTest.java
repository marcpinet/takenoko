package takenoko.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilsTest {
    Random rand;

    @BeforeEach
    void setUp() {
        rand = new Random(0);
    }

    @Test
    void randomPick() {
        var ls = List.of(1, 2, 3, 4, 5);
        assertEquals(1, Utils.randomPick(ls, rand).orElseThrow());
        assertEquals(4, Utils.randomPick(ls, rand).orElseThrow());

        var empty = Collections.emptyList();
        assertEquals(Optional.empty(), Utils.randomPick(empty, rand));
    }
}
