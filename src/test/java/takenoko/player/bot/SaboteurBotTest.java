package takenoko.player.bot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import takenoko.action.Action;
import takenoko.action.ActionValidator;
import takenoko.action.PossibleActionLister;
import takenoko.game.GameInventory;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.tile.*;
import takenoko.utils.Coord;

class SaboteurBotTest {
    private Board board;
    private SaboteurBot bot;
    private PossibleActionLister actionLister;
    private ActionValidator validator;

    private ArrayList<Action> alreadyPlayedActions;

    @BeforeEach
    void setUp() {
        board = new Board();
        var random = new Random(0);
        bot = new SaboteurBot(random, "Saboteur");

        var gameInventory =
                new GameInventory(20, new TileDeck(random), random, new WeatherDice(random));

        alreadyPlayedActions = new ArrayList<>();

        validator =
                new ActionValidator(
                        board,
                        gameInventory,
                        bot.getPrivateInventory(),
                        bot.getVisibleInventory(),
                        WeatherDice.Face.SUN,
                        alreadyPlayedActions);
        actionLister = new PossibleActionLister(board, validator, bot.getPrivateInventory());

        bot.beginTurn(2);
    }

    @Test
    void pickObjectiveAndIrrigationFirstTurn() {
        var action = bot.chooseAction(board, actionLister);

        assertTrue(action instanceof Action.TakeObjective);

        alreadyPlayedActions.add(action);

        action = bot.chooseAction(board, actionLister);
        assertTrue(action instanceof Action.TakeIrrigationStick);
    }

    @Test
    void oftenRetrieveBamboo()
            throws BambooSizeException, BambooIrrigationException, IrrigationException,
                    BoardException {
        var tile = new BambooTile(Color.GREEN);
        board.placeTile(new Coord(0, 1), tile);
        tile.growBamboo();

        // We don't want to take an objective or irrigation stick
        var possibleActions =
                actionLister.getPossibleActions().stream()
                        .filter(Predicate.not(Action.TakeObjective.class::isInstance))
                        .filter(Predicate.not(Action.TakeIrrigationStick.class::isInstance))
                        .toList();

        var lister = mock(PossibleActionLister.class);
        when(lister.getPossibleActions()).thenReturn(possibleActions);

        var action = bot.chooseAction(board, lister);

        assertTrue(
                action instanceof Action.MovePiece movePiece
                        && movePiece.piece() == MovablePiece.PANDA
                        && movePiece.to().equals(new Coord(0, 1)));
    }

    @Test
    void randomValidActionWhenNoBamboo() throws IrrigationException, BoardException {
        var tile = new BambooTile(Color.GREEN);
        board.placeTile(new Coord(0, 1), tile);

        var action = bot.chooseAction(board, actionLister);
        assertTrue(validator.isValid(action));
    }

    @Test
    void chooseWeatherWithRain() {
        var allowedWeathers = Arrays.asList(WeatherDice.Face.values());
        var weather = bot.chooseWeather(allowedWeathers);
        assertEquals(WeatherDice.Face.RAIN, weather);
    }

    @Test
    void chooseRandomWeatherWithNoRain() {
        var allowedWeathers = List.of(WeatherDice.Face.SUN, WeatherDice.Face.CLOUDY);
        var weather = bot.chooseWeather(allowedWeathers);
        assertTrue(allowedWeathers.contains(weather));
    }

    @Test
    void throwsWhenNoPossibleWeather() {
        var allowedWeathers = new ArrayList<WeatherDice.Face>();
        assertThrows(IllegalStateException.class, () -> bot.chooseWeather(allowedWeathers));
    }

    @Test
    void pickWatershedWhenPossible() {
        var pickWatershed = new Action.PickPowerUp(PowerUp.WATERSHED);

        var defaultPossibleActions = new ArrayList<>(actionLister.getPossibleActions());
        defaultPossibleActions.add(pickWatershed);

        var lister = mock(PossibleActionLister.class);
        when(lister.getPossibleActions()).thenReturn(defaultPossibleActions);

        assertEquals(pickWatershed, bot.chooseAction(board, lister));
    }

    @Test
    void endTurnWhenNoPossibleAction() {
        var lister = mock(PossibleActionLister.class);
        when(lister.getPossibleActions()).thenReturn(new ArrayList<>());
        assertEquals(Action.END_TURN, bot.chooseAction(board, lister));
    }
}
