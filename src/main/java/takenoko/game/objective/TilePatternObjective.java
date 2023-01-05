package takenoko.game.objective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.Color;
import takenoko.utils.Coord;

/// This class works by storing a list of deltas from one edge of the pattern.
/// For example, if the pattern is a 2x2 square, the deltas are:
/// (0, 0), (0, 1), (1, 0), (1, 1)
/// The pattern is then rotated to find all possible patterns.
/// This way, we can check if a pattern is achieved by only checking starting from the last tile
// placed.
public class TilePatternObjective implements Objective {
    public static final List<Coord> DIAMOND_4 =
            Arrays.asList(new Coord(0, 0), new Coord(0, 1), new Coord(1, 0), new Coord(1, 1));
    public static final List<Coord> LINE_2 = Arrays.asList(new Coord(0, 0), new Coord(0, 1));
    public static final List<Coord> LINE_3 =
            Arrays.asList(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2));
    public static final List<Coord> TRIANGLE_3 =
            Arrays.asList(new Coord(0, 0), new Coord(0, 1), new Coord(1, 0));

    private final Set<List<Coord>> patternRotations;
    private boolean achieved = false;
    private final Color color;

    /// pattern is an array of deltas from one edge of the pattern to the next, expected to start
    // with (0, 0)
    public TilePatternObjective(Color color, List<Coord> pattern) {
        this.color = color;
        // initial pattern without rotation
        patternRotations =
                generateShifts(pattern).stream()
                        .unordered()
                        // add the shifted patterns
                        .flatMap(pat -> generateRotations(pat).stream())
                        // add the reversed patterns
                        .flatMap(pat -> generateReversed(pat).stream())
                        // remove duplicates
                        .collect(Collectors.toSet());
    }

    /// Generate all possible rotations of a pattern
    private List<List<Coord>> generateRotations(List<Coord> pattern) {
        var rotations = new ArrayList<List<Coord>>();
        rotations.add(pattern);

        // rotate the pattern to find all possible patterns
        final int ROTATION_COUNT = 6;
        for (int i = 1; i < ROTATION_COUNT; ++i) {
            var rotated = new ArrayList<Coord>();
            rotated.add(pattern.get(0));
            for (int j = 1; j < pattern.size(); ++j) {
                rotated.add(rotations.get(i - 1).get(j).rotate(rotated.get(0)));
            }
            rotations.add(rotated);
        }
        return rotations;
    }

    /// Generate all possible patterns by shifting the pattern by one tile.
    private List<List<Coord>> generateShifts(List<Coord> pattern) {
        var shifts = new ArrayList<List<Coord>>();
        shifts.add(pattern);
        for (int i = 1; i < pattern.size(); ++i) {
            var shifted = new ArrayList<Coord>();
            for (int j = 0; j < pattern.size(); ++j) {
                shifted.add(shifts.get(i - 1).get((j + 1) % pattern.size()));
            }
            shifts.add(shifted);
        }
        return shifts;
    }

    /// Generate all possible patterns by reversing the pattern.
    private List<List<Coord>> generateReversed(List<Coord> pattern) {
        var reversed = new ArrayList<List<Coord>>();
        var revertedPattern = new ArrayList<Coord>();
        for (var c : pattern) {
            revertedPattern.add(c.reversed());
        }
        reversed.add(revertedPattern);
        reversed.add(pattern);
        return reversed;
    }

    @Override
    public boolean isAchieved(Board board, Action lastAction) {
        // Once the objective is achieved, it stays achieved
        if (achieved) {
            return true;
        }
        // We know the status can only change if the last action was a tile placement
        if (!(lastAction instanceof Action.PlaceTile placeTile)) {
            return false;
        }
        // Test all possible patterns
        var coord = placeTile.coord();
        achieved =
                patternRotations.stream().anyMatch(pattern -> isPatternAt(board, coord, pattern));
        return achieved;
    }

    @Override
    public boolean wasAchievedAfterLastCheck() {
        return achieved;
    }

    // A tile is part of the pattern if it is the right color and if it's irrigated
    private boolean isValidTile(Board board, Coord coord) {
        try {
            var tile = board.getTile(coord);
            if (tile instanceof BambooTile bambooTile) {
                return bambooTile.getColor() == color && bambooTile.isIrrigated();
            }
        } catch (BoardException e) {
            // The tile is not on the board, so it's not part of the pattern
        }
        return false;
    }

    private boolean isPatternAt(Board board, Coord coord, List<Coord> pattern) {
        return pattern.stream().map(coord::add).allMatch(c -> isValidTile(board, c));
    }

    public Color getColor() {
        return color;
    }
}
