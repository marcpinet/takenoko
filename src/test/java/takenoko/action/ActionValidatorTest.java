package takenoko.action;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import takenoko.game.GameInventory;
import takenoko.game.WeatherDice;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.MovablePiece;
import takenoko.game.board.VisibleInventory;
import takenoko.game.objective.BambooSizeObjective;
import takenoko.game.objective.Objective;
import takenoko.game.tile.*;
import takenoko.player.PrivateInventory;
import takenoko.utils.Coord;

class ActionValidatorTest {
    private Board board;
    private GameInventory gameInventory;
    private ActionValidator validator;
    private ArrayList<Action> previousActions;
    private PrivateInventory privateInventory;
    private VisibleInventory visibleInventory;

    @BeforeEach
    void setUp() {
        board = new Board();
        privateInventory = new PrivateInventory();
        visibleInventory = new VisibleInventory();
        visibleInventory.incrementIrrigation();

        gameInventory =
                new GameInventory(
                        20,
                        new TileDeck(new Random(0)),
                        new Random(0),
                        new WeatherDice(new Random(0)));

        previousActions = new ArrayList<>();
        resetWeather(WeatherDice.Face.SUN);
    }

    void resetWeather(WeatherDice.Face weather) {
        validator =
                new ActionValidator(
                        board,
                        gameInventory,
                        privateInventory,
                        visibleInventory,
                        weather,
                        previousActions);
    }

    @Test
    void none() {
        assertTrue(validator.isValid(Action.NONE));
    }

    @ParameterizedTest
    @MethodSource("placeTileProvider")
    void placeTile(Coord coord, BambooTile tile, boolean expectedResult) {
        var action = new Action.PlaceTile(coord, TileDeck.DEFAULT_DRAW_PREDICATE);
        assertEquals(expectedResult, validator.isValid(action));
    }

    private static Stream<Arguments> placeTileProvider() {
        return Stream.of(
                Arguments.of(new Coord(0, 1), new BambooTile(Color.GREEN), true),
                Arguments.of(new Coord(0, 0), new BambooTile(Color.GREEN), false),
                Arguments.of(new Coord(2, 2), new BambooTile(Color.GREEN), false));
    }

    @ParameterizedTest
    @MethodSource("placeIrrigationProvider")
    void placeIrrigation(Coord coord, TileSide side, boolean expectedResult)
            throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        var action = new Action.PlaceIrrigationStick(coord, side);
        assertEquals(expectedResult, validator.isValid(action));
    }

    private static Stream<Arguments> placeIrrigationProvider() {
        return Stream.of(
                Arguments.of(new Coord(0, 1), TileSide.UP_LEFT, true, true),
                Arguments.of(new Coord(0, 0), TileSide.UP_LEFT, false, false),
                Arguments.of(new Coord(2, 2), TileSide.UP_LEFT, false, false));
    }

    @Test
    void placeIrrigationWhenNotEnough() throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        var action = new Action.PlaceIrrigationStick(new Coord(0, 1), TileSide.UP_LEFT);
        validator =
                new ActionValidator(
                        board,
                        gameInventory,
                        new PrivateInventory(),
                        new VisibleInventory(),
                        WeatherDice.Face.SUN);
        assertFalse(validator.isValid(action));
    }

    @Test
    void takeIrrigation() {
        var action = new Action.TakeIrrigationStick();
        assertTrue(validator.isValid(action));
    }

    @Test
    void takeIrrigationWhenNotEnough() {
        var validator =
                new ActionValidator(
                        board,
                        new GameInventory(
                                0,
                                new TileDeck(new Random(0)),
                                new Random(0),
                                new WeatherDice(new Random(0))),
                        new PrivateInventory(),
                        new VisibleInventory(),
                        WeatherDice.Face.SUN);
        var action = new Action.TakeIrrigationStick();
        assertFalse(validator.isValid(action));
    }

    @ParameterizedTest
    @MethodSource("unveilObjectiveProvider")
    void unveilObjective(Objective obj, boolean expectedResult) {
        when(obj.isAchieved()).thenReturn(expectedResult);
        var action = new Action.UnveilObjective(obj);
        assertEquals(expectedResult, validator.isValid(action));
    }

    private static Stream<Arguments> unveilObjectiveProvider() {
        var objMock1 = mock(BambooSizeObjective.class);
        var objMock2 = mock(BambooSizeObjective.class);
        return Stream.of(Arguments.of(objMock1, true), Arguments.of(objMock2, false));
    }

    private static Stream<Arguments> moveGardenerProvider() {
        return Stream.of(
                Arguments.of(new Coord(0, 1), true),
                Arguments.of(new Coord(0, 0), false), // already there
                Arguments.of(new Coord(1, 1), false),
                Arguments.of(new Coord(99, 99), false));
    }

    @ParameterizedTest
    @MethodSource("moveGardenerProvider")
    void moveGardener(Coord coord, boolean expectedResult)
            throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 0), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 1), new BambooTile(Color.GREEN));
        var action = new Action.MovePiece(MovablePiece.GARDENER, coord);
        assertEquals(expectedResult, validator.isValid(action));
    }

    private static Stream<Arguments> movePandaProvider() {
        return Stream.of(
                Arguments.of(new Coord(0, 1), true),
                Arguments.of(new Coord(0, 0), false), // already there
                Arguments.of(new Coord(2, 2), false),
                Arguments.of(new Coord(99, 99), false));
    }

    @ParameterizedTest
    @MethodSource("movePandaProvider")
    void movePanda(Coord coord, boolean expectedResult)
            throws IrrigationException, BoardException, PowerUpException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 0), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 1), new BambooTile(Color.GREEN));
        var action = new Action.MovePiece(MovablePiece.PANDA, coord);
        assertEquals(expectedResult, validator.isValid(action));
        if (board.getTile(new Coord(1, 1)) instanceof BambooTile bambooTile) {
            bambooTile.setPowerUp(PowerUp.ENCLOSURE);
        }
        var secondAction = new Action.MovePiece(MovablePiece.PANDA, new Coord(1, 1));
        assertFalse(validator.isValid(secondAction));
    }

    @ParameterizedTest
    @MethodSource("twiceActionProvider")
    void twiceAction(Action action1, Action action2) {
        assertTrue(validator.isValid(action1));
        assertTrue(validator.isValid(action2));

        previousActions.add(action1);

        assertFalse(validator.isValid(action1));
        assertFalse(validator.isValid(action2));
    }

    private static Stream<Arguments> twiceActionProvider() {
        return Stream.of(
                Arguments.of(
                        new Action.PlaceTile(new Coord(0, 1), TileDeck.DEFAULT_DRAW_PREDICATE),
                        new Action.PlaceTile(new Coord(1, 0), TileDeck.DEFAULT_DRAW_PREDICATE)),
                Arguments.of(
                        new Action.TakeObjective(Objective.Type.BAMBOO_SIZE),
                        new Action.TakeObjective(Objective.Type.HARVESTING)));
    }

    @Test
    void twiceActionWithWind() {
        resetWeather(WeatherDice.Face.WIND);

        var action = new Action.PlaceTile(new Coord(0, 1), TileDeck.DEFAULT_DRAW_PREDICATE);
        assertTrue(validator.isValid(action));

        previousActions.add(action);

        var action2 = new Action.PlaceTile(new Coord(1, 0), TileDeck.DEFAULT_DRAW_PREDICATE);
        assertTrue(validator.isValid(action2));
    }

    @Test
    void movePandaAndGardenerAreNotLinked() throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile(Color.GREEN));
        board.placeTile(new Coord(1, 0), new BambooTile(Color.GREEN));

        var action1 = new Action.MovePiece(MovablePiece.PANDA, new Coord(0, 1));
        var action2 = new Action.MovePiece(MovablePiece.GARDENER, new Coord(1, 0));

        assertTrue(validator.isValid(action1));
        assertTrue(validator.isValid(action2));

        previousActions.add(action1);

        assertFalse(validator.isValid(action1));
        assertTrue(validator.isValid(action2));
    }
}
