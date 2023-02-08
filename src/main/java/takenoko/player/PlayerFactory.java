package takenoko.player;

import java.util.Random;
import takenoko.player.bot.EasyBot;

public class PlayerFactory {
    private PlayerFactory() {}

    public static Player makePlayer(PlayerType type, String name, Random random) {
        return switch (type) {
            case RANDOM -> new EasyBot(random, name);
        };
    }
}
