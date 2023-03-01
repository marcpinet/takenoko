package takenoko.player.bot;

import java.util.List;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.player.PlayerBase;

public class DefaultBot extends PlayerBase<DefaultBot> implements PlayerBase.PlayerBaseInterface {
    public Action chooseActionImpl(Board board, PossibleActionLister actionLister) {
        return Action.END_TURN;
    }

    @Override
    public WeatherDice.Face chooseWeatherImpl(List<WeatherDice.Face> allowedWeathers) {
        return WeatherDice.Face.SUN;
    }
}
