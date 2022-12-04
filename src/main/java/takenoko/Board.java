package takenoko;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Coord, Tile> tiles;

    public Board() {
        tiles = new HashMap<>();
        tiles.put(new Coord(0, 0), new PondTile());
    }

    public void placeTile(Coord c, Tile t) throws BoardException {

        if (!c.isAdjacentTo(new Coord(0, 0))) {
            throw new BoardException("Erreur : tuile non-adjacente.");
        }
        if (tiles.containsKey(c)) {
            throw new BoardException("Erreur : Il y a déjà une tuile présente aux coordonnées c.");
        }
        tiles.put(c, t);
    }

    public Tile getTile(Coord c) throws BoardException {
        if (!tiles.containsKey(c)) {
            throw new BoardException(
                    "Erreur : la tuile avec les coord c n'est pas présente sur le plateau.");
        }
        return tiles.get(c);
    }
}
