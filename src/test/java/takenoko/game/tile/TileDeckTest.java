package takenoko.game.tile;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.game.Deck;

class TileDeckTest {
    TileDeck deck;
    private static final int DECK_SIZE = 10;

    @BeforeEach
    void setUp() {
        ArrayDeque<Tile> tiles = new ArrayDeque<>();
        for (int i = 0; i < DECK_SIZE; i++) {
            if (i % 2 != 0) {
                tiles.add(new PondTile());
            } else {
                tiles.add(new BambooTile(Color.GREEN));
            }
        }
        deck = new TileDeck(tiles);
    }

    @Test
    void defaultConstructor() {
        var deck = new TileDeck(new Random(0));
        assertEquals(27, deck.size());
    }

    @Test
    void size() {
        assertEquals(DECK_SIZE, deck.size());
    }

    @Test
    void draw() throws EmptyDeckException {
        assertEquals(new BambooTile(Color.GREEN), deck.draw(TileDeck.DEFAULT_DRAW_PREDICATE));

        assertEquals(DECK_SIZE - 1, deck.size());

        assertEquals(new PondTile(), deck.draw(TileDeck.DEFAULT_DRAW_PREDICATE));

        assertEquals(DECK_SIZE - 2, deck.size());
    }

    @Test
    void lessThanThreeTiles() throws EmptyDeckException {
        deck = new TileDeck(new ArrayDeque<>(List.of(new BambooTile(Color.GREEN), new PondTile())));
        Deck.DrawPredicate<Tile> predicate =
                tiles -> {
                    assertEquals(2, tiles.size());
                    return 0;
                };
        deck.draw(predicate);
    }

    @Test
    void emptyDeck() throws EmptyDeckException {
        for (int i = 0; i < DECK_SIZE; i++) {
            deck.draw(TileDeck.DEFAULT_DRAW_PREDICATE);
        }
        assertThrows(EmptyDeckException.class, () -> deck.draw(TileDeck.DEFAULT_DRAW_PREDICATE));
    }
}
