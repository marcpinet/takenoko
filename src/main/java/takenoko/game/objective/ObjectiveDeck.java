package takenoko.game.objective;

import java.util.*;
import takenoko.game.Deck;
import takenoko.game.tile.BambooSizeException;
import takenoko.game.tile.Color;
import takenoko.game.tile.EmptyDeckException;
import takenoko.game.tile.PowerUp;

public class ObjectiveDeck extends Deck<Objective> {
    static final int DRAW_SIZE = 1;

    public ObjectiveDeck(Deque<Objective> elements) {
        super(elements, DRAW_SIZE);
    }

    public static ObjectiveDeck makeTilePatternObjectiveDeck(Random random) {
        var list = new ArrayList<TilePatternObjective>();

        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.TRIANGLE, 2));
        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.DIAMOND, 3));
        list.add(
                new TilePatternObjective(
                        List.of(Color.YELLOW, Color.YELLOW, Color.PINK, Color.PINK),
                        TilePatternObjective.DIAMOND,
                        5));
        list.add(
                new TilePatternObjective(
                        List.of(Color.PINK, Color.PINK, Color.GREEN, Color.GREEN),
                        TilePatternObjective.DIAMOND,
                        4));
        list.add(
                new TilePatternObjective(
                        List.of(Color.YELLOW, Color.YELLOW, Color.GREEN, Color.GREEN),
                        TilePatternObjective.DIAMOND,
                        3));
        list.add(new TilePatternObjective(Color.YELLOW, TilePatternObjective.LINE_3, 3));
        list.add(new TilePatternObjective(Color.YELLOW, TilePatternObjective.DIAMOND, 4));
        list.add(
                new TilePatternObjective(
                        Color.YELLOW, TilePatternObjective.DIAMOND_WITHOUT_RIGHT_PART, 3));
        list.add(new TilePatternObjective(Color.GREEN, TilePatternObjective.LINE_3, 2));
        list.add(
                new TilePatternObjective(
                        Color.GREEN, TilePatternObjective.DIAMOND_WITHOUT_RIGHT_PART, 2));
        list.add(
                new TilePatternObjective(
                        Color.PINK, TilePatternObjective.DIAMOND_WITHOUT_RIGHT_PART, 4));
        list.add(new TilePatternObjective(Color.PINK, TilePatternObjective.TRIANGLE, 4));
        list.add(new TilePatternObjective(Color.PINK, TilePatternObjective.DIAMOND, 5));
        list.add(new TilePatternObjective(Color.PINK, TilePatternObjective.LINE_3, 4));
        list.add(new TilePatternObjective(Color.YELLOW, TilePatternObjective.TRIANGLE, 3));

        Collections.shuffle(list, random);

        return new ObjectiveDeck(new ArrayDeque<>(list));
    }

    public static ObjectiveDeck makeBambooSizeObjectiveDeck(Random random) {
        var list = new ArrayList<BambooSizeObjective>();

        try {
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.YELLOW, 5, PowerUpNecessity.MANDATORY, PowerUp.WATERSHED));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.YELLOW, 6, PowerUpNecessity.FORBIDDEN, PowerUp.NONE));
            list.add(
                    new BambooSizeObjective(
                            2, 3, Color.PINK, 6, PowerUpNecessity.NO_MATTER, PowerUp.NONE));
            list.add(
                    new BambooSizeObjective(
                            3, 3, Color.YELLOW, 7, PowerUpNecessity.NO_MATTER, PowerUp.NONE));
            list.add(
                    new BambooSizeObjective(
                            4, 3, Color.GREEN, 8, PowerUpNecessity.NO_MATTER, PowerUp.NONE));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.GREEN, 4, PowerUpNecessity.MANDATORY, PowerUp.ENCLOSURE));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.GREEN, 4, PowerUpNecessity.MANDATORY, PowerUp.WATERSHED));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.GREEN, 5, PowerUpNecessity.FORBIDDEN, PowerUp.NONE));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.YELLOW, 4, PowerUpNecessity.MANDATORY, PowerUp.FERTILIZER));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.YELLOW, 5, PowerUpNecessity.MANDATORY, PowerUp.ENCLOSURE));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.PINK, 5, PowerUpNecessity.MANDATORY, PowerUp.FERTILIZER));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.PINK, 6, PowerUpNecessity.MANDATORY, PowerUp.WATERSHED));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.PINK, 6, PowerUpNecessity.MANDATORY, PowerUp.ENCLOSURE));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.PINK, 7, PowerUpNecessity.FORBIDDEN, PowerUp.NONE));
            list.add(
                    new BambooSizeObjective(
                            1, 4, Color.GREEN, 3, PowerUpNecessity.MANDATORY, PowerUp.FERTILIZER));
        } catch (BambooSizeException e) {
            // Should never happen
            throw new IllegalStateException(e);
        }

        Collections.shuffle(list, random);

        return new ObjectiveDeck(new ArrayDeque<>(list));
    }

    public static ObjectiveDeck makeHarvestingObjectiveDeck(Random random) {
        var list = new ArrayList<HarvestingObjective>();
        for (int i = 0; i < 5; i++) {
            list.add(new HarvestingObjective(2, 0, 0, 3));
        }
        for (int i = 0; i < 4; i++) {
            list.add(new HarvestingObjective(0, 2, 0, 4));
        }
        for (int i = 0; i < 3; i++) {
            list.add(new HarvestingObjective(0, 0, 2, 5));
            list.add(new HarvestingObjective(1, 1, 1, 6));
        }

        Collections.shuffle(list, random);

        return new ObjectiveDeck(new ArrayDeque<>(list));
    }

    public Objective draw() throws EmptyDeckException {
        // We always draw one element, since that's the only size we support
        DrawPredicate<Objective> pickFirstTile = ignored -> 0;
        return super.draw(pickFirstTile);
    }
}
