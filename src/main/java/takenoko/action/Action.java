package takenoko.action;

import java.util.List;
import java.util.Map;
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

    record None() implements Action {
        @Override
        public String toString() {
            return "Nothing happened.";
        }
    }

    record EndTurn() implements Action {
        @Override
        public String toString() {
            return "End of the current turn.";
        }

        @Override
        public boolean hasCost() {
            return false;
        }
    }

    record PlaceTile(Coord coord, Deck.DrawPredicate<Tile> drawPredicate) implements Action {}

    record TakeTilePatternObjective() implements Action {}

    record TakeHarvestingObjective() implements Action {}

    record TakeBambooSizeObjective() implements Action {}

    record UnveilObjective(Objective objective) implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }
    }

    record TakeIrrigationStick() implements Action {}

    record PlaceIrrigationStick(Coord coord, TileSide side) implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }
    }

    record MovePiece(MovablePiece piece, Coord to) implements Action {}

    record BeginSimulation() implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }
    }

    record EndSimulation() implements Action {
        @Override
        public boolean hasCost() {
            return false;
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
    }

    record PlacePowerUp(Coord coord, PowerUp powerUp) implements Action {
        @Override
        public boolean hasCost() {
            return false;
        }
    }
}
