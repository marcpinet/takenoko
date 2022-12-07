package takenoko.bot;

import takenoko.Action;
import takenoko.Board;
import takenoko.Player;

public class DefaultBot implements Player {
    @Override
    public Action chooseAction(Board board) {
        return Action.NONE;
    }
}
