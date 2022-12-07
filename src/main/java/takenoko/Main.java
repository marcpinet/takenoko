package takenoko;

import java.util.List;
import java.util.logging.Logger;
import takenoko.bot.DefaultBot;

public class Main {
    public static void main(String... args) {
        List<Player> players = List.of(new DefaultBot(), new DefaultBot());
        var game = new Game(players, Logger.getGlobal());
        game.play();
    }
}
