package takenoko.game.objective;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import takenoko.game.Deck;
import takenoko.game.tile.BambooSizeException;
import takenoko.game.tile.Color;
import takenoko.game.tile.EmptyDeckException;
import takenoko.game.tile.PowerUp;

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
