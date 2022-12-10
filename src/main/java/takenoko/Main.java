package takenoko;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import takenoko.bot.EasyBot;

public class Main {
    public static void main(String... args) {
        List<Player> players = List.of(new EasyBot(new Random()), new EasyBot(new Random()));
        var game = new Game(players, Logger.getGlobal());
        game.play();
    }
}
