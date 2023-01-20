package takenoko.game.objective;

import java.util.Queue;
import takenoko.game.Deck;
import takenoko.game.tile.EmptyDeckException;

public class ObjectiveDeck<O extends Objective> extends Deck<O> {
    static final int DRAW_SIZE = 1;

    public ObjectiveDeck(Queue<O> elements) {
        super(elements, 1);
    }

    public O draw() throws EmptyDeckException {
        return super.draw(ignored -> 0);
    }
}
