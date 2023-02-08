package takenoko.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVHandler {

    private static final Path PATH = Paths.get("stats/gamestats.csv");
    private Simulator.SimStats stats;
    private static final String[] HEADERS = {
        "Player Type",
        "Victory count",
        "Victory percentage",
        "Lost count",
        "Lost percentage",
        "Draw count",
        "Draw percentage",
        "Average score",
        "Average completed objective",
        "Average turn count"
    };

    public CSVHandler(Simulator.SimStats stats) {
        this.stats = stats;
    }

    private static boolean ensureExistence() {
        if (Files.notExists(PATH.getParent())) {
            try {
                Files.createDirectory(PATH.getParent());
            } catch (Exception ignored) {
            }
        }

        if (Files.notExists(PATH)) {
            try {
                Files.createFile(PATH);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private List<String[]> readAllLines(Path filePath) throws Exception {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return csvReader.readAll();
            }
        }
    }

    private String[][] generateData() {
        String[][] data = new String[stats.players().size()][HEADERS.length];

        for (int i = 0; i < stats.players().size(); i++) {
            var player = stats.players().get(i);
            int numWins = stats.getNumWins().get(player);
            double percentWins = stats.getPercentWins().get(player);
            int numLosses = stats.getNumLosses().get(player);
            double percentLosses = stats.getPercentLosses().get(player);
            int numDraws = stats.getNumDraws().get(player);
            double percentDraws = stats.getPercentDraws().get(player);
            double avgScore = stats.getAvgScore().get(player);
            double avgCompletedObjective = stats.getAvgObjective().get(player);
            double avgTurnCount = stats.getAvgTurn();
            data[i][0] = player;
            data[i][1] = String.valueOf(numWins);
            data[i][2] = String.valueOf(Math.round(percentWins));
            data[i][3] = String.valueOf(numLosses);
            data[i][4] = String.valueOf(Math.round(percentLosses));
            data[i][5] = String.valueOf(numDraws);
            data[i][6] = String.valueOf(Math.round(percentDraws));
            data[i][7] = String.valueOf(avgScore);
            data[i][8] = String.valueOf(avgCompletedObjective);
            data[i][9] = String.valueOf(avgTurnCount);
        }

        return data;
    }

    public void write() throws IOException {
        boolean alreadyExists = ensureExistence();

        File file = new File(PATH.toString());
        FileWriter outputfile = new FileWriter(file);

        String[][] data = this.generateData();

        try (CSVWriter writer = new CSVWriter(outputfile)) {

            if (!alreadyExists) {
                writer.writeNext(HEADERS);
            }

            for (String[] row : data) {
                writer.writeNext(row);
            }
        }
    }
}
