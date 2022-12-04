package takenoko;

import java.util.Arrays;

public record Coord(int x, int y) {

    public int z() {
        return (-x - y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coord c)) return false;
        return (x == c.x && y == c.y);
    }

    public Coord[] adjacentCoords() {
        Coord[] adjacentCoords = new Coord[6];
        adjacentCoords[0] = new Coord(x, y + 1);
        adjacentCoords[1] = new Coord(x + 1, y);
        adjacentCoords[2] = new Coord(x + 1, y - 1);
        adjacentCoords[3] = new Coord(x, y - 1);
        adjacentCoords[4] = new Coord(x - 1, y);
        adjacentCoords[5] = new Coord(x - 1, y + 1);
        return adjacentCoords;
    }

    public boolean isAdjacentTo(Coord c) {
        return Arrays.asList(adjacentCoords()).contains(c);
    }
}
