package takenoko;

public record Coord(int x, int y) {

    public int z() {
        return (-x - y);
    }
}
