package takenoko.game;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import takenoko.game.tile.EmptyDeckException;

public abstract class Deck<T> {
    private final Deque<T> elements;
    private final int drawSize;

    public interface DrawPredicate<T> extends Function<List<T>, Integer> {}

    protected Deck(Deque<T> elements, int drawSize) {
        this.elements = elements;
        this.drawSize = drawSize;
    }

    private ArrayList<T> prepareDraw() throws EmptyDeckException {
        if (elements.isEmpty()) {
            throw new EmptyDeckException("Deck is empty.");
        }
        int drawCount = Math.min(drawSize, elements.size());

        var availableTiles = new ArrayList<T>(drawCount);
        for (int i = 0; i < drawCount; ++i) {
            availableTiles.add(elements.poll());
        }
        return availableTiles;
    }

    public T draw(DrawPredicate<T> predicate) throws EmptyDeckException {
        var availableTs = prepareDraw();
        int pickedIndex = predicate.apply(availableTs);

        var res = availableTs.get(pickedIndex);
        availableTs.remove(pickedIndex);

        // put back the tiles that were not picked
        elements.addAll(availableTs);

        return res;
    }

    public int simulateDraw(DrawPredicate<T> predicate) throws EmptyDeckException {
        var availableTs = prepareDraw();
        int pickedIndex = predicate.apply(availableTs);

        // put back the tiles
        elements.addAll(availableTs);

        return pickedIndex;
    }

    public void addFirst(T element) {
        elements.addFirst(element);
    }

    public int size() {
        return elements.size();
    }
}
