package takenoko.player;

import java.util.List;
import takenoko.action.Action;
import takenoko.action.PossibleActionLister;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.VisibleInventory;

/**
 * This interface is used to define player. In this project, all our players are bots. But it could
 * work with human players too!
 *
 * <p>The basic idea is that the game will call the methods of this interface in the right order.
 * The game will call the method beginTurn() at the beginning of the turn. Then, the game will call
 * the method chooseAction() to ask the player to choose an action. This method will be called until
 * the player ends his turn.
 */
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

    String getName();
}
