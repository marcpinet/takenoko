package takenoko;

import java.util.Objects;
import takenoko.objective.Objective;

public sealed interface Action permits Action.None, Action.PlaceTile, Action.UnveilObjective {
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

    record UnveilObjective(Objective objective) implements Action {
        @Override
        public int cost() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof UnveilObjective other) {
                return objective.equals(other.objective);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(objective);
        }
    }
}
