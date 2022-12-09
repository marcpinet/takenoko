package takenoko;

import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
            if (o instanceof PlaceTile other) {
                return coord.equals(other.coord) && tile.equals(other.tile);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, tile);
        }
    }
}
