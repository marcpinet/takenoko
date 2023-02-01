package takenoko.action;

import java.util.Objects;
import takenoko.game.Deck;
import takenoko.game.objective.Objective;
import takenoko.game.tile.PowerUp;
import takenoko.game.tile.Tile;
import takenoko.game.tile.TileSide;
import takenoko.utils.Coord;

public sealed interface Action
        permits Action.EndTurn,
                Action.MoveGardener,
                Action.MovePanda,
                Action.None,
                Action.PickPowerUp,
                Action.PlaceIrrigationStick,
                Action.PlaceTile,
                Action.TakeBambooSizeObjective,
                Action.TakeHarvestingObjective,
                Action.TakeIrrigationStick,
                Action.TakeTilePatternObjective,
                Action.UnveilObjective {
    Action NONE = new Action.None();
    Action END_TURN = new Action.EndTurn();

    default boolean hasCost() {
        return true;
    }

    default boolean isSameTypeAs(Action other) {
        return this.getClass() == other.getClass();
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

    record PlaceTile(Coord coord, Deck.DrawPredicate<Tile> drawPredicate) implements Action {
        @Override
        public boolean equals(Object o) {
            if (o instanceof PlaceTile other) {
                return coord.equals(other.coord) && drawPredicate.equals(other.drawPredicate);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, drawPredicate);
        }
    }

    record TakeTilePatternObjective() implements Action {
        @Override
        public boolean equals(Object o) {
            return o instanceof TakeTilePatternObjective;
        }
    }

    record TakeHarvestingObjective() implements Action {
        @Override
        public boolean equals(Object o) {
            return o instanceof TakeHarvestingObjective;
        }
    }

    record TakeBambooSizeObjective() implements Action {
        @Override
        public boolean equals(Object o) {
            return o instanceof TakeBambooSizeObjective;
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

    record PickPowerUp(PowerUp powerUp) implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PickPowerUp other) {
                return powerUp.equals(other.powerUp);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(powerUp);
        }
    }
}
