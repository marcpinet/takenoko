package takenoko;

public sealed interface Action permits Action.None, Action.PlaceTile {
    Action NONE = new Action.None();

    int cost();

    final class None implements Action {
        @Override
        public int cost() {
            return 1;
        }
    }

    record PlaceTile(Coord coord, Tile tile) implements Action {
        @Override
        public int cost() {
            return 1;
        }
    }
}
