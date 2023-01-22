package takenoko.game.objective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.VisibleInventory;
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
    private record Element(Color color, Coord coord) {
        public Element offset(Coord offset) {
            return new Element(color, coord.add(offset));
        }

        public Element rotate(Coord center) {
            return new Element(color, coord.rotate(center));
        }

        public Element reversed() {
            return new Element(color, coord.reversed());
        }
    }

    public static final List<Coord> DIAMOND_4 =
            List.of(new Coord(0, 0), new Coord(0, 1), new Coord(1, 0), new Coord(1, 1));
    public static final List<Coord> LINE_2 = List.of(new Coord(0, 0), new Coord(0, 1));
    public static final List<Coord> LINE_3 =
            List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2));
    public static final List<Coord> TRIANGLE_3 =
            List.of(new Coord(0, 0), new Coord(0, 1), new Coord(1, 0));

    private final Set<List<Element>> patternVariations;
    private boolean achieved = false;
    private final int score;

    /// pattern is an array of deltas from one edge of the pattern to the next, expected to start
    /// with (0, 0)
    public TilePatternObjective(List<Color> color, List<Coord> pattern, int score) {
        if (pattern.size() != color.size()) {
            throw new IllegalArgumentException("Pattern and color must have the same size");
        }

        // mix the pattern with the color
        var patternWithColors = new ArrayList<Element>();
        for (int i = 0; i < pattern.size(); ++i) {
            patternWithColors.add(new Element(color.get(i), pattern.get(i)));
        }

        // initial pattern without rotation
        patternVariations =
                generateShifts(patternWithColors).stream()
                        .unordered()
                        // add the shifted patterns
                        .flatMap(pat -> generateRotations(pat).stream())
                        // add the reversed patterns
                        .flatMap(pat -> generateReversed(pat).stream())
                        // remove duplicates
                        .collect(Collectors.toSet());
        this.score = score;
    }

    /// alternative constructor for a pattern with only one color
    public TilePatternObjective(Color color, List<Coord> pattern, int score) {
        this(Collections.nCopies(pattern.size(), color), pattern, score);
    }

    public TilePatternObjective(Color color, List<Coord> pattern) {
        this(Collections.nCopies(pattern.size(), color), pattern, 1);
    }

    /// Generate all possible rotations of a pattern
    private List<List<Element>> generateRotations(List<Element> pattern) {
        var rotations = new ArrayList<List<Element>>();
        rotations.add(pattern);

        // rotate the pattern to find all possible patterns
        final int ROTATION_COUNT = 6;
        for (int i = 1; i < ROTATION_COUNT; ++i) {
            var rotated = new ArrayList<Element>();
            rotated.add(pattern.get(0));
            for (int j = 1; j < pattern.size(); ++j) {
                rotated.add(rotations.get(i - 1).get(j).rotate(rotated.get(0).coord()));
            }
            rotations.add(rotated);
        }
        return rotations;
    }

    /// Generate all possible patterns by shifting the pattern by one tile.
    private List<List<Element>> generateShifts(List<Element> pattern) {
        var shifts = new ArrayList<List<Element>>();
        shifts.add(pattern);
        for (int i = 1; i < pattern.size(); ++i) {
            var shifted = new ArrayList<Element>();
            for (int j = 0; j < pattern.size(); ++j) {
                shifted.add(shifts.get(i - 1).get((j + 1) % pattern.size()));
            }
            shifts.add(shifted);
        }
        return shifts;
    }

    /// Generate all possible patterns by reversing the pattern.
    private List<List<Element>> generateReversed(List<Element> pattern) {
        var reversed = new ArrayList<List<Element>>();
        var revertedPattern = new ArrayList<Element>();
        for (var el : pattern) {
            revertedPattern.add(el.reversed());
        }
        reversed.add(revertedPattern);
        reversed.add(pattern);
        return reversed;
    }

    @Override
    public boolean computeAchieved(Board board, Action lastAction, VisibleInventory ignored) {
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
                patternVariations.stream().anyMatch(pattern -> isPatternAt(board, coord, pattern));
        return achieved;
    }

    @Override
    public boolean isAchieved() {
        return achieved;
    }

    @Override
    public int getScore() {
        return score;
    }

    // A tile is part of the pattern if it is the right color and if it's irrigated
    private boolean isValidTile(Board board, Element element) {
        try {
            var tile = board.getTile(element.coord());
            if (tile instanceof BambooTile bambooTile) {
                return bambooTile.getColor() == element.color() && bambooTile.isIrrigated();
            }
        } catch (BoardException e) {
            // The tile is not on the board, so it's not part of the pattern
        }
        return false;
    }

    private boolean isPatternAt(Board board, Coord coord, List<Element> pattern) {
        return pattern.stream().map(el -> el.offset(coord)).allMatch(el -> isValidTile(board, el));
    }
}
