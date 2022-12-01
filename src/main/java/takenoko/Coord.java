package takenoko;

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
}
