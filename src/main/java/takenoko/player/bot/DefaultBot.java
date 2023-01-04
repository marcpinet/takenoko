package takenoko.player.bot;

import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.game.board.Board;
import takenoko.player.PlayerBase;

public class DefaultBot extends PlayerBase<DefaultBot> implements PlayerBase.PlayerBaseInterface {
    public Action chooseActionImpl(Board board, ActionValidator validator) {
        return Action.END_TURN;
    }
}
