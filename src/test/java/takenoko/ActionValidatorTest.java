package takenoko;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import takenoko.objective.Objective;

class ActionValidatorTest {
    private Board board;
    private static final int STICK_COUNT = 20;
    private ActionValidator validator;

    @BeforeEach
    void setUp() {
        board = new Board();
        validator = new ActionValidator(board, STICK_COUNT);
    }

    @Test
    void testNone() {
        assertTrue(validator.isValid(Action.NONE));
    }

    @ParameterizedTest
    @MethodSource("placeTileProvider")
    void testPlaceTile(Coord coord, BambooTile tile, boolean expectedResult) {
        var action = new Action.PlaceTile(coord, tile);
        assertEquals(expectedResult, validator.isValid(action));
    }

    private static Stream<Arguments> placeTileProvider() {
        return Stream.of(
                Arguments.of(new Coord(0, 1), new BambooTile(), true),
                Arguments.of(new Coord(0, 0), new BambooTile(), false),
                Arguments.of(new Coord(2, 2), new BambooTile(), false));
    }

    @ParameterizedTest
    @MethodSource("placeIrrigationProvider")
    void testPlaceIrrigation(Coord coord, TileSide side, boolean expectedResult)
            throws IrrigationException, BoardException {
        board.placeTile(new Coord(0, 1), new BambooTile());
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
    void testTakeIrrigation() {
        var action = new Action.TakeIrrigationStick();
        assertTrue(validator.isValid(action));
    }

    @Test
    void testTakeIrrigationWhenNotEnough() {
        var validator = new ActionValidator(board, 0);
        var action = new Action.TakeIrrigationStick();
        assertFalse(validator.isValid(action));
    }

    @ParameterizedTest
    @MethodSource("unveilObjectiveProvider")
    void testUnveilObjective(Objective obj, boolean expectedResult) {
        when(obj.wasAchievedAfterLastCheck()).thenReturn(expectedResult);
        var action = new Action.UnveilObjective(obj);
        assertEquals(expectedResult, validator.isValid(action));
    }

    private static Stream<Arguments> unveilObjectiveProvider() {
        var objMock1 = mock(Objective.class);
        var objMock2 = mock(Objective.class);
        return Stream.of(Arguments.of(objMock1, true), Arguments.of(objMock2, false));
    }
}
