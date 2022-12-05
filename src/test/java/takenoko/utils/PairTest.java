package takenoko.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PairTest {
    private static final String FIRST = "first";
    private static final Integer SECOND = 2;
    Pair<String, Integer> pair;

    @BeforeEach
    void setUp() {
        pair = Pair.of(FIRST, SECOND);
    }

    @Test
    void testPair() {
        assertEquals(FIRST, pair.first());
        assertEquals(SECOND, pair.second());
    }
}
