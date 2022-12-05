package takenoko;

import java.util.List;

public class Main {
    public static void main(String... args) {
        List<Player> players = List.of(new DefaultBot(), new DefaultBot());
        var game = new Game(players, System.out);
        game.play();
    }
}
