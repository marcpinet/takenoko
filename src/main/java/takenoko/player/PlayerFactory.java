package takenoko.player;

import java.util.Random;
import takenoko.player.bot.PlotRushBot;
import takenoko.player.bot.RandomBot;

public class PlayerFactory {
    private PlayerFactory() {}

    public static Player makePlayer(PlayerType type, String name, Random random) {
        return switch (type) {
            case RANDOM -> new RandomBot(random, name);
            case PLOT_RUSH -> new PlotRushBot(random, name);
        };
    }
}
