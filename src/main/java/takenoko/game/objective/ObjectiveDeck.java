package takenoko.game.objective;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import takenoko.game.Deck;
import takenoko.game.tile.BambooSizeException;
import takenoko.game.tile.Color;
import takenoko.game.tile.EmptyDeckException;

public class ObjectiveDeck<O extends Objective> extends Deck<O> {
    static final int DRAW_SIZE = 1;

    public static ObjectiveDeck<TilePatternObjective> makeTilePatternObjectiveDeck() {
        var list = new ArrayList<TilePatternObjective>();

        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.DIAMOND_4));
        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_2));
        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3));
        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.TRIANGLE_3));

        return new ObjectiveDeck<>(new ArrayDeque<>(list));
    }

    public static ObjectiveDeck<BambooSizeObjective> makeBambooSizeObjectiveDeck() {
        var list = new ArrayList<BambooSizeObjective>();

        try {
            list.add(new BambooSizeObjective(1, 1, Color.GREEN));
            list.add(new BambooSizeObjective(1, 2, Color.GREEN));
            list.add(new BambooSizeObjective(1, 3, Color.GREEN));
            list.add(new BambooSizeObjective(1, 4, Color.GREEN));
            list.add(new BambooSizeObjective(2, 1, Color.GREEN));
        } catch (BambooSizeException e) {
            // Should never happen
            throw new IllegalStateException(e);
        }

        return new ObjectiveDeck<>(new ArrayDeque<>(list));
    }

    public static ObjectiveDeck<HarvestingObjective> makeHarvestingObjectiveDeck() {
        var list = new ArrayList<HarvestingObjective>();

        list.add(new HarvestingObjective(1, 1, 1));
        list.add(new HarvestingObjective(1, 0, 2));
        list.add(new HarvestingObjective(0, 0, 3));
        list.add(new HarvestingObjective(0, 1, 4));

        return new ObjectiveDeck<>(new ArrayDeque<>(list));
    }

    public ObjectiveDeck(Queue<O> elements) {
        super(elements, DRAW_SIZE);
    }

    public O draw() throws EmptyDeckException {
        // We always draw one element, since that's the only size we support
        DrawPredicate<O> pickFirstTile = ignored -> 0;
        return super.draw(pickFirstTile);
    }
}
