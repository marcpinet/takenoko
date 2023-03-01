package takenoko.main;

import static java.util.logging.Level.OFF;
import static org.junit.jupiter.api.Assertions.*;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.player.PlayerType;

class CSVHandlerTest {
    private CSVHandler csv;

    @BeforeEach
    void setUp() {
        var logger = Logger.getGlobal();
        logger.setLevel(OFF);
        var sim =
                new Simulator(
                        5,
                        List.of(PlayerType.PLOT_RUSH, PlayerType.RANDOM, PlayerType.SABOTEUR),
                        logger,
                        new Random(0));
        var res = sim.simulate();
        csv = new CSVHandler(res);
    }

    @Test
    void write() throws IOException, CsvException {
        csv.setPath("stats/test.csv");

        csv.write();

        assertTrue(csv.getFilePath().toFile().exists());

        // checking the content of the file is not empty
        assertTrue(csv.getFilePath().toFile().length() > 0);

        // removing after the tests
        assertTrue(csv.getFilePath().toFile().delete());
    }
}
