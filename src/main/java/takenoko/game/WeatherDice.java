package takenoko.game;

import java.util.Random;

public class WeatherDice {

    private final Random random;

    public WeatherDice(Random random) {
        this.random = random;
    }

    public Face throwDice() {
        return Face.values()[random.nextInt(Face.values().length)];
    }

    public enum Face {
        SUN,
        RAIN,
        WIND,
        THUNDERSTORM,
        CLOUDY,
        ANY
    }
}
