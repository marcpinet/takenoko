package takenoko.game.tile;

import java.util.*;

public class TileDeck extends takenoko.game.Deck<Tile> {
    private static final int DRAW_SIZE = 3;
    public static final DrawPredicate<Tile> DEFAULT_DRAW_PREDICATE = ignored -> 0;

    public TileDeck(Random random) {
        this(generateOfficialTiles(random));
    }

    private static Queue<Tile> generateOfficialTiles(Random random) {
        List<Tile> tempTiles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            tempTiles.add(new BambooTile(Color.GREEN, PowerUp.NONE));
        }
        for (int i = 0; i < 2; i++) {
            tempTiles.add(new BambooTile(Color.GREEN, PowerUp.WATERSHED));
        }
        for (int i = 0; i < 2; i++) {
            tempTiles.add(new BambooTile(Color.GREEN, PowerUp.ENCLOSURE));
        }
        tempTiles.add(new BambooTile(Color.GREEN, PowerUp.FERTILIZER));

        for (int i = 0; i < 4; i++) {
            tempTiles.add(new BambooTile(Color.PINK, PowerUp.NONE));
        }
        tempTiles.add(new BambooTile(Color.PINK, PowerUp.WATERSHED));
        tempTiles.add(new BambooTile(Color.PINK, PowerUp.FERTILIZER));
        tempTiles.add(new BambooTile(Color.PINK, PowerUp.ENCLOSURE));

        for (int i = 0; i < 6; i++) {
            tempTiles.add(new BambooTile(Color.YELLOW, PowerUp.NONE));
        }
        tempTiles.add(new BambooTile(Color.YELLOW, PowerUp.WATERSHED));
        tempTiles.add(new BambooTile(Color.YELLOW, PowerUp.FERTILIZER));
        tempTiles.add(new BambooTile(Color.YELLOW, PowerUp.ENCLOSURE));

        Collections.shuffle(tempTiles, random);
        return new ArrayDeque<>(tempTiles);
    }

    public TileDeck(Queue<Tile> tiles) {
        super(tiles, DRAW_SIZE);
    }
}
