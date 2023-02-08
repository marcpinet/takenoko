package takenoko.main;

import static java.util.logging.Level.OFF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import takenoko.player.PlayerType;

class SimulatorTest {

    @Test
    void testSimulator() {
        var logger = Logger.getGlobal();
        logger.setLevel(OFF);
        var sim =
                new Simulator(
                        20, List.of(PlayerType.RANDOM, PlayerType.RANDOM), logger, new Random(0));
        var res = sim.simulate();
        assertEquals(20, res.nbGames());
        assertEquals(2, res.players().size());
        assertEquals(6, res.getNumWins().get("Mireille"));
        assertEquals(10, res.getNumWins().get("Philippe"));
    }
}
