package takenoko.bot;

import takenoko.Action;
import takenoko.ActionValidator;
import takenoko.Board;
import takenoko.PlayerBase;

public class DefaultBot extends PlayerBase<DefaultBot> implements PlayerBase.PlayerBaseInterface {
    public Action chooseActionImpl(Board board, ActionValidator validator) {
        return Action.NONE;
    }

    @Override
    public boolean wantsToEndTurn() {
        return true;
    }
}
