package takenoko;

import takenoko.utils.Pair;

public class DefaultBot implements Player {
    @Override
    public Pair<Action, Action> chooseActions(Board board) {
        return Pair.of(Action.NONE, Action.NONE);
    }
}
