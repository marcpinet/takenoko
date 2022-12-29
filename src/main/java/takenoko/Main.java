package takenoko;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import takenoko.game.Game;
import takenoko.game.TooManyTurnsException;
import takenoko.game.objective.Objective;
import takenoko.game.objective.TilePatternObjective;
import takenoko.game.tile.TileDeck;
import takenoko.player.Player;
import takenoko.player.bot.EasyBot;

public class Main {
    public static void main(String... args) throws TooManyTurnsException {
        List<Player> players = List.of(new EasyBot(new Random()), new EasyBot(new Random()));
        List<Objective> objectives =
                List.of(
                        new TilePatternObjective(TilePatternObjective.TRIANGLE_3),
                        new TilePatternObjective(TilePatternObjective.DIAMOND_4),
                        new TilePatternObjective(TilePatternObjective.LINE_3),
                        new TilePatternObjective(TilePatternObjective.LINE_2));
        var tileDeck = new TileDeck();
        var game = new Game(players, objectives, Logger.getGlobal(), tileDeck);
        game.play();
    }
}
