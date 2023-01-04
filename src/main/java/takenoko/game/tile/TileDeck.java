package takenoko.game.tile;

import java.util.*;
import java.util.function.Function;

public class TileDeck {
    private static final int DRAW_SIZE = 3;
    private final Queue<Tile> tiles;

    public interface DrawTilePredicate extends Function<List<Tile>, Integer> {}

    public static final DrawTilePredicate DEFAULT_DRAW_TILE_PREDICATE = ignored -> 0;

    public TileDeck(Random random) {
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

        tiles = new ArrayDeque<>(tempTiles);
    }

    public TileDeck(Queue<Tile> tiles) {
        this.tiles = tiles;
    }

    public Tile draw(DrawTilePredicate predicate) throws EmptyTileDeckException {
        if (tiles.isEmpty()) {
            throw new EmptyTileDeckException("Tile deck is empty.");
        }
        // picking a tile in Takenoko means choosing between the first three tiles
        int drawCount = Math.min(DRAW_SIZE, tiles.size());

        var availableTiles = new ArrayList<Tile>(drawCount);
        for (int i = 0; i < drawCount; ++i) {
            availableTiles.add(tiles.poll());
        }
        int pickedIndex = predicate.apply(availableTiles);

        var res = availableTiles.get(pickedIndex);
        availableTiles.remove(pickedIndex);

        // put back the tiles that were not picked
        tiles.addAll(availableTiles);

        return res;
    }

    public int size() {
        return tiles.size();
    }
}
