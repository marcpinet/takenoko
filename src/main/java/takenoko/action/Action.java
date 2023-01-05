package takenoko.action;

import java.util.Objects;
import takenoko.game.objective.Objective;
import takenoko.game.tile.TileDeck;
import takenoko.game.tile.TileSide;
import takenoko.utils.Coord;

public sealed interface Action
        permits Action.MoveGardener,
                Action.MovePanda,
                Action.None,
                Action.PlaceIrrigationStick,
                Action.PlaceTile,
                Action.TakeIrrigationStick,
                Action.UnveilObjective,
                Action.EndTurn {
    Action NONE = new Action.None();
    Action END_TURN = new Action.EndTurn();

    default boolean hasCost() {
        return true;
    }

    final class None implements Action {
        private None() {}
    }

    final class EndTurn implements Action {
        private EndTurn() {}

        @Override
        public boolean hasCost() {
            return false;
        }
    }

    record PlaceTile(Coord coord, TileDeck.DrawTilePredicate drawTilePredicate) implements Action {
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
        public boolean hasCost() {
            return false;
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
        public boolean hasCost() {
            return false;
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
