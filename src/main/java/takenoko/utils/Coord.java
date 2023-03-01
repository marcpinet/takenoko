package takenoko.utils;

import java.util.Arrays;
import takenoko.game.tile.TileSide;

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

    // 0 : up, 1 : up-right, 2 : down-right, 3 : down, 4 : down-left, 5 : up-left
    public Coord[] adjacentCoords() {
        Coord[] adjacentCoords = new Coord[6];
        adjacentCoords[0] = new Coord(x, y - 1);
        adjacentCoords[1] = new Coord(x + 1, y - 1);
        adjacentCoords[2] = new Coord(x + 1, y);
        adjacentCoords[3] = new Coord(x, y + 1);
        adjacentCoords[4] = new Coord(x - 1, y + 1);
        adjacentCoords[5] = new Coord(x - 1, y);
        return adjacentCoords;
    }

    public boolean isAdjacentTo(Coord c) {
        return Arrays.asList(adjacentCoords()).contains(c);
    }

    public Coord add(Coord other) {
        return new Coord(x + other.x, y + other.y);
    }

    public Coord negated() {
        return new Coord(-x, -y);
    }

    /// Rotates the coordinate around the center
    /// See https://www.redblobgames.com/grids/hexagons/#rotation
    /// Here's the full recipe for rotating a position hex around a center position center to result
    // in a new
    /// position rotated:
    ///    Convert positions hex and center to cube coordinates.
    ///    Calculate a vector by subtracting the center: vec = cube_subtract(hex, center) =
    // Cube(hex.q - center.q, hex
    ///    .r - center.r, hex.s - center.s).
    ///    Rotate the vector vec as described above, and call the resulting vector rotated_vec.
    ///    Convert the vector back to a position by adding the center: rotated =
    // cube_add(rotated_vec, center) = Cube
    ///    (rotated_vec.q + center.q, rotated_vec.r + center.r, rotated_vec.s + center.s).
    ///    Convert the cube position rotated back to your preferred coordinate system.
    public Coord rotate(Coord center) {
        var q = x - center.x;
        var s = z() - center.z();

        var rotatedQ = -s;
        var rotatedR = -q;

        return new Coord(rotatedQ + center.x, rotatedR + center.y);
    }

    public Coord adjacentCoordSide(TileSide side) {
        return adjacentCoords()[side.ordinal()];
    }

    public boolean isAlignedWith(Coord c) {
        return x == c.x || y == c.y || z() == c.z();
    }
}
