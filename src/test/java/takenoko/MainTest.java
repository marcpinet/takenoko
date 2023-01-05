package takenoko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

    String varToBeInitInSetup;

    @BeforeEach
    void setUp() {
        varToBeInitInSetup = "Hello World!";
    }

    @Test
    void helloTest() {
        assertEquals("Hello World!", varToBeInitInSetup);
    }
}
