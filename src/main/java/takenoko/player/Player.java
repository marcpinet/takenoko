package takenoko.player;

import java.util.List;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

public interface Player {
    void beginTurn(int actionCredits);

    int availableActionCredits();

    Action chooseAction(Board board, PossibleActionLister actionLister);

    PrivateInventory getPrivateInventory();

    VisibleInventory getVisibleInventory();

    void increaseScore(int delta);

    int getScore();

    void decreaseScore(int score);

    WeatherDice.Face chooseWeather(List<WeatherDice.Face> allowedWeathers);
}
