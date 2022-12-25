package takenoko;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

public class TileDeck {
    private static final int DRAW_SIZE = 3;
    private final Queue<Tile> tiles;

    public interface DrawTilePredicate extends Function<List<Tile>, Integer> {}

    public static final DrawTilePredicate DEFAULT_DRAW_TILE_PREDICATE = ignored -> 0;

    // default game content
    public static final int DEFAULT_SIZE = 27;

    public TileDeck(TileFactory tileFactory, int numTiles) {
        tiles = new ArrayDeque<>(numTiles);
        for (int i = 0; i < numTiles; ++i) {
            tiles.add(tileFactory.randomTile());
        }
    }

    public TileDeck(TileFactory tileFactory) {
        this(tileFactory, DEFAULT_SIZE);
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
