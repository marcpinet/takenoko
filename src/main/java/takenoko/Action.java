package takenoko;

public sealed interface Action permits Action.None, Action.PlaceTile {
    public final Action NONE = new Action.None();

    public final class None implements Action {}

    public record PlaceTile(Coord coord, Tile tile) implements Action {}
}
