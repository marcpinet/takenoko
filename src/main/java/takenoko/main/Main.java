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
        JCommander.newBuilder().addObject(args2).build().parse(args);
        if (args2.isDemo() || args.length == 0) {
            demo();
        }
        if (args2.isTwoThousands()) {
            simulate();
        }
    }

    public static void demo() {
        List<Player> players =
                List.of(
                        new RandomBot(new Random(), "edgar"),
                        new PlotRushBot(new Random(), "marc"));
        var tileDeck = new TileDeck(new Random());
        var logger = Logger.getGlobal();
        ConsoleHandler handler = new ConsoleHandler();
        logger.addHandler(handler);
        LogFormatter formatter = new LogFormatter();
        logger.setUseParentHandlers(false);
        handler.setFormatter(formatter);
        var game = new Game(players, logger, tileDeck, new WeatherDice(new Random()), new Random());
        var winner = game.play();

        if (winner.isPresent()) {
            logger.info("There is a winner: " + winner.get());
        } else {
            logger.info("No winner");
        }
    }

    public static void simulate() {

        var logger = Logger.getGlobal();
        logger.setLevel(Level.OFF);

        Simulator simulator =
                new Simulator(
                        500,
                        List.of(PlayerType.RANDOM, PlayerType.RANDOM),
                        logger,
                        Simulator.Parallelism.YES);

        logger.setLevel(Level.INFO);
        logger.log(Level.INFO, "{0}", simulator.simulate());
    }
}
