package takenoko;

import takenoko.utils.Pair;

public interface Player {
    Pair<Action, Action> chooseActions(Board board);
}
