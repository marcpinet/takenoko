package takenoko.main;

import com.beust.jcommander.JCommander;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import takenoko.game.Game;
import takenoko.game.WeatherDice;
import takenoko.game.tile.TileDeck;
import takenoko.player.Player;
import takenoko.player.PlayerType;
import takenoko.player.bot.PlotRushBot;
import takenoko.player.bot.RandomBot;
import takenoko.utils.LogFormatter;

public class Main {

    public static void main(String... args) {
        Args args2 = new Args();

        var logger = Logger.getGlobal();
        ConsoleHandler handler = new ConsoleHandler();
        logger.addHandler(handler);
        LogFormatter formatter = new LogFormatter();
        logger.setUseParentHandlers(false);
        handler.setFormatter(formatter);

        JCommander.newBuilder().addObject(args2).build().parse(args);
        if (args2.isDemo() || args.length == 0) {
            demo(logger);
        }
        if (args2.isTwoThousands()) {
            simulate(logger);
        }
        if (args2.isCsv()) {
            generateCsv(logger);
        }
    }

    public static void demo(Logger logger) {
        List<Player> players =
                List.of(
                        new RandomBot(new Random(), "edgar"),
                        new PlotRushBot(new Random(), "marc"));
        var tileDeck = new TileDeck(new Random());
        var game = new Game(players, logger, tileDeck, new WeatherDice(new Random()), new Random());
        var winner = game.play();

        if (winner.isPresent()) {
            logger.info("There is a winner: " + winner.get());
        } else {
            logger.info("No winner");
        }
    }

    public static void simulate(Logger logger) {

        logger.setLevel(Level.OFF);

        Simulator simulator1 =
                new Simulator(
                        1000,
                        List.of(PlayerType.PLOT_RUSH, PlayerType.SABOTEUR),
                        logger,
                        Simulator.Parallelism.YES);

        Simulator simulator2 =
                new Simulator(
                        1000,
                        List.of(PlayerType.PLOT_RUSH, PlayerType.PLOT_RUSH),
                        logger,
                        Simulator.Parallelism.YES);

        var res1 = simulator1.simulate();
        var res2 = simulator2.simulate();

        logger.setLevel(Level.INFO);
        logger.log(Level.INFO, "{0}", res1);
        logger.log(Level.INFO, "{0}", res2);
    }

    public static void generateCsv(Logger logger) {
        logger.setLevel(Level.OFF);

        var simulator =
                new Simulator(
                        300,
                        List.of(PlayerType.PLOT_RUSH, PlayerType.SABOTEUR, PlayerType.RANDOM),
                        logger,
                        Simulator.Parallelism.YES);
        Simulator.SimStats stats = simulator.simulate();

        CSVHandler csvHandler = new CSVHandler(stats);
        try {
            csvHandler.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
