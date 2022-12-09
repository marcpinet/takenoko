package takenoko.bot;

import takenoko.Action;
import takenoko.Board;
import takenoko.PlayerBase;

public class DefaultBot extends PlayerBase<DefaultBot> implements PlayerBase.PlayerBaseInterface {
    public Action chooseActionImpl(Board board) {
        return Action.NONE;
    }

    @Override
    public boolean wantsToEndTurn() {
        return true;
    }
}
