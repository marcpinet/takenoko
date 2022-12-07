package takenoko;

public sealed interface Action permits Action.None, Action.PlaceTile {
    Action NONE = new Action.None();

    final class None implements Action {}

    record PlaceTile(Coord coord, Tile tile) implements Action {}
}
