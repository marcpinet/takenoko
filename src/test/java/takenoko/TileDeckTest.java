package takenoko;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileDeckTest {
    TileDeck deck;
    private static final int DECK_SIZE = 10;

    @BeforeEach
    void setUp() {
        // mock the tile factory to always return the same tiles
        var factory =
                new TileFactory() {
                    int c = 0;

                    @Override
                    public Tile randomTile() {
                        return c++ % 2 == 0 ? new BambooTile() : new PondTile();
                    }
                };

        deck = new TileDeck(factory, DECK_SIZE);
    }

    @Test
    void size() {
        assertEquals(DECK_SIZE, deck.size());
    }

    @Test
    void draw() {
        try {
            assertEquals(new BambooTile(), deck.draw(TileDeck.DEFAULT_DRAW_TILE_PREDICATE));
        } catch (EmptyTileDeckException e) {
            e.printStackTrace();
        }
        assertEquals(DECK_SIZE - 1, deck.size());
        try {
            assertEquals(new PondTile(), deck.draw(TileDeck.DEFAULT_DRAW_TILE_PREDICATE));
        } catch (EmptyTileDeckException e) {
            e.printStackTrace();
        }
        assertEquals(DECK_SIZE - 2, deck.size());
    }

    @Test
    void lessThanThreeTiles() {
        deck = new TileDeck(new TileFactory(), 2);
        TileDeck.DrawTilePredicate predicate =
                tiles -> {
                    assertEquals(2, tiles.size());
                    return 0;
                };
    }

    @Test
    void emptyDeck() {
        for (int i = 0; i < DECK_SIZE; i++) {
            try {
                deck.draw(TileDeck.DEFAULT_DRAW_TILE_PREDICATE);
            } catch (EmptyTileDeckException e) {
                e.printStackTrace();
                fail();
            }
        }
        assertThrows(
                EmptyTileDeckException.class,
                () -> deck.draw(TileDeck.DEFAULT_DRAW_TILE_PREDICATE));
    }
}
