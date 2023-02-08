package takenoko.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    private Path path = Paths.get("stats/gamestats.csv");
    private Simulator.SimStats stats;
    private static final String[] HEADERS = {
        "Player Type",
        "Player Name",
        "Victory count",
        "Victory percentage",
        "Lost count",
        "Lost percentage",
        "Draw count",
        "Draw percentage",
        "Average score",
        "Average completed objective",
        "Average turn count",
        "Game count"
    };

    public CSVHandler(Simulator.SimStats stats) {
        this.stats = stats;
    }

    private boolean ensureExistence() {
        if (Files.notExists(path.getParent())) {
            try {
                Files.createDirectory(path.getParent());
            } catch (FileAlreadyExistsException ignored) {
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        // Checking if gamestats.csv does exist in the stats folder
        return path.toFile().exists();
    }

    private List<String[]> readAllEntriesExceptHeader(Path filePath)
            throws CsvException, IOException {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                var content = csvReader.readAll();
                content.remove(0);
                return content;
            }
        }
    }

    private List<String[]> generateData() {
        List<String[]> data = new ArrayList<>();

        for (int i = 0; i < stats.players().size(); i++) {
            data.add(new String[HEADERS.length]);
            String player = stats.players().get(i);
            String botType = stats.botTypes().get(player).toString();
            int numWins = stats.getNumWins().get(player);
            double percentWins = stats.getPercentWins().get(player);
            int numLosses = stats.getNumLosses().get(player);
            double percentLosses = stats.getPercentLosses().get(player);
            int numDraws = stats.getNumDraws().get(player);
            double percentDraws = stats.getPercentDraws().get(player);
            double avgScore = stats.getAvgScore().get(player);
            double avgCompletedObjective = stats.getAvgObjective().get(player);
            double avgTurnCount = stats.getAvgTurn();
            int nbGames = stats.nbGames();

            data.get(i)[0] = botType;
            data.get(i)[1] = player;
            data.get(i)[2] = String.valueOf(numWins);
            data.get(i)[3] = String.valueOf(percentWins);
            data.get(i)[4] = String.valueOf(numLosses);
            data.get(i)[5] = String.valueOf(percentLosses);
            data.get(i)[6] = String.valueOf(numDraws);
            data.get(i)[7] = String.valueOf(percentDraws);
            data.get(i)[8] = String.valueOf(avgScore);
            data.get(i)[9] = String.valueOf(avgCompletedObjective);
            data.get(i)[10] = String.valueOf(avgTurnCount);
            data.get(i)[11] = String.valueOf(nbGames);
        }

        return data;
    }

    private void mergeData(List<String[]> data, List<String[]> data2) {
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            String[] row2 = data2.get(i);

            // Merging the game count to easily calculate the averages
            int oldNbGame = Integer.parseInt(row[11]);
            row[11] = String.valueOf(Integer.parseInt(row[11]) + Integer.parseInt(row2[11]));

            // starting at 2 because we don't want to merge the player type and name
            row[2] = String.valueOf(Integer.parseInt(row[2]) + Integer.parseInt(row2[2]));
            row[3] = String.valueOf(Double.parseDouble(row[2]) / Integer.parseInt(row[11]) * 100);
            row[4] = String.valueOf(Integer.parseInt(row[4]) + Integer.parseInt(row2[4]));
            row[5] = String.valueOf(Double.parseDouble(row[4]) / Integer.parseInt(row[11]) * 100);
            row[6] = String.valueOf(Integer.parseInt(row[6]) + Integer.parseInt(row2[6]));
            row[7] = String.valueOf(Double.parseDouble(row[6]) / Integer.parseInt(row[11]) * 100);
            row[8] =
                    String.valueOf(
                            (Double.parseDouble(row[8]) * oldNbGame
                                            + Double.parseDouble(row2[8])
                                                    * Integer.parseInt(row2[11]))
                                    / Integer.parseInt(row[11]));
            row[9] =
                    String.valueOf(
                            (Double.parseDouble(row[9]) * oldNbGame
                                            + Double.parseDouble(row2[9])
                                                    * Integer.parseInt(row2[11]))
                                    / Integer.parseInt(row[11]));
            row[10] =
                    String.valueOf(
                            (Double.parseDouble(row[10]) * oldNbGame
                                            + Double.parseDouble(row2[10])
                                                    * Integer.parseInt(row2[11]))
                                    / Integer.parseInt(row[11]));
        }
    }

    public void write() throws CsvException, IOException {
        boolean alreadyExists = ensureExistence();

        File file = new File(path.toString() + ".tmp");
        FileWriter outputFile = new FileWriter(file);

        List<String[]> data = this.generateData();

        try (CSVWriter writer = new CSVWriter(outputFile)) {

            writer.writeNext(HEADERS);

            // if the file already exists, we merge the data
            if (alreadyExists) {
                List<String[]> data2 = readAllEntriesExceptHeader(path);
                mergeData(data, data2); // Merging data2 into data
            }

            for (String[] row : data) {
                writer.writeNext(row);
            }
        }

        // Deleting old file
        Files.delete(path);

        // Renaming new file
        if (!file.renameTo(path.toFile())) throw new IOException("Could not rename file");
    }

    public Path getFilePath() {
        return path;
    }

    public void setPath(String path) {
        this.path = Paths.get(path);
    }
}
