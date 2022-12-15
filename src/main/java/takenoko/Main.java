package takenoko;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import takenoko.bot.EasyBot;
import takenoko.objective.Objective;
import takenoko.objective.TilePatternObjective;

public class Main {
    public static void main(String... args) {
        List<Player> players = List.of(new EasyBot(new Random()), new EasyBot(new Random()));
        List<Objective> objectives =
                List.of(
                        new TilePatternObjective(TilePatternObjective.TRIANGLE_2x2),
                        new TilePatternObjective(TilePatternObjective.SQUARE_2x2),
                        new TilePatternObjective(TilePatternObjective.LINE_3),
                        new TilePatternObjective(TilePatternObjective.LINE_2));
        var game = new Game(players, objectives, Logger.getGlobal());
        game.play();
    }
}
