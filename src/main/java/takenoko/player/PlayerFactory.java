package takenoko.player;

import java.util.Random;
import takenoko.player.bot.EasyBot;

public class PlayerFactory {
    public static Player makePlayer(PlayerType type, String name) {
        return switch (type) {
            case RANDOM -> new EasyBot(new Random(), name);
        };
    }
}
