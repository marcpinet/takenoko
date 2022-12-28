package takenoko;

import java.util.Objects;
import takenoko.objective.Objective;

public sealed interface Action
        permits Action.MoveGardener,
                Action.MovePanda,
                Action.None,
                Action.PlaceIrrigationStick,
                Action.PlaceTile,
                Action.TakeIrrigationStick,
                Action.UnveilObjective {
    Action NONE = new Action.None();

    int cost();

    final class None implements Action {
        @Override
        public int cost() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof None;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    record PlaceTile(Coord coord, TileDeck.DrawTilePredicate drawTilePredicate) implements Action {
        @Override
        public int cost() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PlaceTile other) {
                return coord.equals(other.coord)
                        && drawTilePredicate.equals(other.drawTilePredicate);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, drawTilePredicate);
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

    record TakeIrrigationStick() implements Action {
        @Override
        public int cost() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof TakeIrrigationStick;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    record PlaceIrrigationStick(Coord coord, TileSide side) implements Action {
        @Override
        public int cost() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PlaceIrrigationStick other) {
                return coord.equals(other.coord) && side.equals(other.side);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, side);
        }
    }

    record MoveGardener(Coord coord) implements Action {
        @Override
        public int cost() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MoveGardener other) {
                return coord.equals(other.coord);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord);
        }
    }

    record MovePanda(Coord coord) implements Action {
        @Override
        public int cost() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MovePanda other) {
                return coord.equals(other.coord);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord);
        }
    }
}
