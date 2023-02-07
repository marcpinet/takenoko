package takenoko.action;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import takenoko.game.Deck;
import takenoko.game.board.MovablePiece;
import takenoko.game.objective.Objective;
import takenoko.game.tile.PowerUp;
import takenoko.game.tile.Tile;
import takenoko.game.tile.TileSide;
import takenoko.utils.Coord;

public sealed interface Action
        permits Action.PickPowerUp,
                Action.BeginSimulation,
                Action.EndSimulation,
                Action.EndTurn,
                Action.MovePiece,
                Action.None,
                Action.PlaceIrrigationStick,
                Action.PlacePowerUp,
                Action.PlaceTile,
                Action.SimulateActions,
                Action.TakeBambooSizeObjective,
                Action.TakeHarvestingObjective,
                Action.TakeIrrigationStick,
                Action.TakeTilePatternObjective,
                Action.UnveilObjective {
    Action NONE = new Action.None();
    Action END_TURN = new Action.EndTurn();
    Action.BeginSimulation BEGIN_SIMULATION = new Action.BeginSimulation();
    Action.EndSimulation END_SIMULATION = new Action.EndSimulation();

    default boolean hasCost() {
        return true;
    }

    default boolean isSameTypeAs(Action other) {
        return this.getClass() == other.getClass();
    }

    final class None implements Action {
        private None() {}

        @Override
        public String toString() {
            return "Nothing happened.";
        }
    }

    final class EndTurn implements Action {
        private EndTurn() {}

        @Override
        public String toString() {
            return "End of the current turn.";
        }

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

    record MovePiece(MovablePiece piece, Coord to) implements Action {
        @Override
        public boolean equals(Object o) {
            if (o instanceof MovePiece other) {
                return piece.equals(other.piece) && to.equals(other.to);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(piece, to);
        }
    }

    record BeginSimulation() implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof BeginSimulation;
        }
    }

    record EndSimulation() implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof EndSimulation;
        }
    }

    /// Simulates a list of actions. They will be tried alternatively, not sequentially
    record SimulateActions(
            List<Action> alternativeActions,
            Map<Action, Map<Objective, Objective.Status>> outObjectiveStatus)
            implements Action {
        @Override
        public boolean hasCost() {
            return false;
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

    record PlacePowerUp(Coord coord, PowerUp powerUp) implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PlacePowerUp other) {
                return coord.equals(other.coord) && powerUp.equals(other.powerUp);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, powerUp);
        }
    }
}
