package takenoko.game.objective;

import java.util.*;
import java.util.stream.Collectors;
import takenoko.action.Action;
import takenoko.game.board.Board;
import takenoko.game.board.BoardException;
import takenoko.game.board.VisibleInventory;
import takenoko.game.tile.BambooTile;
import takenoko.game.tile.Color;
import takenoko.utils.Coord;

// spotless:off (spotless destroys the ascii art)
/** This class works by storing a list of deltas from one edge of the pattern.
For example, if the pattern is a 2x2 square, the deltas are:
(0, 0), (0, 1), (1, 0), (1, 1)
The pattern is then rotated to find all possible patterns.
This way, we can check if a pattern is achieved by only checking starting from the last tile
placed.
# Algorithm
The important part is the generation of the pattern variations.
Let's use an example. Black hexagons are the tiles we want to match, white hexagons are the others
The `DIAMOND_WITHOUT_RIGHT_PART`, which is :
 ```raw
  _____         _____         _____
 /     \       /     \       /     \
/ -2,0  \_____/  ===  \_____/  2,0  \
\       /     \       /     \       /
 \_____/  ===  \_____/  1,1  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  0,1  \_____/  2,1  \
\       /     \       /     \       /
 \_____/  ===  \_____/  1,2  \_____/
 /     \       /     \       /     \
/ -2,2  \_____/  0,2  \_____/  2,2  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
```
The deltas are:
```
(0, 0), (-1, 1), (-1, 2)
```
 The first step is to find all possible rotations of the pattern
 Rotation 1:
```raw
  _____         _____         _____
 /     \       /     \       /     \
/ -2,0  \_____/  ===  \_____/  2,0  \
\       /     \       /     \       /
 \_____/ -1,1  \_____/  1,1  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  ===  \_____/  2,1  \
\       /     \       /     \       /
 \_____/ -1,2  \_____/  ===  \_____/
 /     \       /     \       /     \
/ -2,2  \_____/  0,2  \_____/  2,2  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
``` Rotation 2:
```raw
  _____         _____         _____
 /     \       /     \       /     \
/ -2,0  \_____/  ===  \_____/  ===  \
\       /     \       /     \       /
 \_____/ -1,1  \_____/  ===  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  0,1  \_____/  2,1  \
\       /     \       /     \       /
 \_____/ -1,2  \_____/  1,2  \_____/
 /     \       /     \       /     \
/ -2,2  \_____/  0,2  \_____/  2,2  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
``` Rotation 3:
```raw
         _____         _____
        /     \       /     \
  _____/ -1,-2 \_____/  1,-2 \_____
 /     \       /     \       /     \
/ -2,-2 \_____/  0,-2 \_____/  2,-2 \
\       /     \       /     \       /
 \_____/ -1,-1 \_____/  ===  \_____/
 /     \       /     \       /     \
/ -2,-1 \_____/  0,-1 \_____/  2,-1 \
\       /     \       /     \       /
 \_____/ -1,0  \_____/  ===  \_____/
 /     \       /     \       /     \
/ -2,0  \_____/  ===  \_____/  2,0  \
\       /     \       /     \       /
 \_____/ -1,1  \_____/  1,1  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  0,1  \_____/  2,1  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
``` Rotation 4:
```raw
         _____         _____
        /     \       /     \
  _____/ -1,-2 \_____/  1,-2 \_____
 /     \       /     \       /     \
/ -2,-2 \_____/  0,-2 \_____/  2,-2 \
\       /     \       /     \       /
 \_____/  ===  \_____/  1,-1 \_____/
 /     \       /     \       /     \
/ -2,-1 \_____/  ===  \_____/  2,-1 \
\       /     \       /     \       /
 \_____/ -1,0  \_____/  1,0  \_____/
 /     \       /     \       /     \
/ -2,0  \_____/  ===  \_____/  2,0  \
\       /     \       /     \       /
 \_____/ -1,1  \_____/  1,1  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  0,1  \_____/  2,1  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
``` Rotation 5:
```raw
         _____         _____
        /     \       /     \
  _____/ -1,-2 \_____/  1,-2 \_____
 /     \       /     \       /     \
/ -2,-2 \_____/  0,-2 \_____/  2,-2 \
\       /     \       /     \       /
 \_____/ -1,-1 \_____/  1,-1 \_____/
 /     \       /     \       /     \
/ -2,-1 \_____/  0,-1 \_____/  2,-1 \
\       /     \       /     \       /
 \_____/  ===  \_____/  1,0  \_____/
 /     \       /     \       /     \
/  ===  \_____/  ===  \_____/  2,0  \
\       /     \       /     \       /
 \_____/ -1,1  \_____/  1,1  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  0,1  \_____/  2,1  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
 ```
There we go for the rotations... or do we?
This is not enough! We need to do the rotations, starting from the second tile too!
 For example, Rotation 1.1
```raw
  _____         _____         _____
 /     \       /     \       /     \
/ -2,0  \_____/  ===  \_____/  2,0  \
\       /     \       /     \       /
 \_____/ -1,1  \_____/  1,1  \_____/
 /     \       /     \       /     \
/ -2,1  \_____/  ===  \_____/  2,1  \
\       /     \       /     \       /
 \_____/  ===  \_____/  ---  \_____/
 /     \       /     \       /     \
/ -2,2  \_____/  0,2  \_____/  2,2  \
\       /     \       /     \       /
 \_____/       \_____/       \_____/
 ```
The "---" represents the original tile, and the "===" represents the new one.
As we can see, the second tile has moved independently from the first one
Fortunately, we do not have to generate more rotations. If we look more closely, we can see that Rotation 1.1 is
the same as Rotation 3, with a translation. Translations are handled easily thanks to our use of offsets.

 Generation of the rotations is done by `generateRotations`

However, we're not done yet. The algorithm has to be able to start from any tile, and not only from the first one.
So this means we have to use each tile in the pattern as the starting point for the algorithm.
This is handled by `generateShifts`
**/
// spotless:on
public class TilePatternObjective implements Objective {
    private record Element(Color color, Coord coord) {
        public Element offset(Coord offset) {
            return new Element(color, coord.add(offset));
        }

        public Element rotate(Coord center) {
            return new Element(color, coord.rotate(center));
        }
    }

    public static final List<Coord> LINE_2 = List.of(new Coord(0, 0), new Coord(0, 1));

    // Officials objectives
    public static final List<Coord> TRIANGLE =
            List.of(new Coord(0, 0), new Coord(0, 1), new Coord(-1, 1));
    public static final List<Coord> DIAMOND_WITHOUT_RIGHT_PART =
            List.of(new Coord(0, 0), new Coord(-1, 1), new Coord(-1, 2));
    public static final List<Coord> DIAMOND =
            List.of(new Coord(0, 0), new Coord(0, 1), new Coord(-1, 1), new Coord(-1, 2));
    public static final List<Coord> LINE_3 =
            List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2));

    private final Set<List<Element>> patternVariations;
    private Status status;
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
                generateRotations(patternWithColors).stream()
                        .unordered()
                        // add the shifted patterns
                        .flatMap(pat -> generateShifts(pat).stream())
                        // remove potential duplicates
                        .collect(Collectors.toSet());
        this.score = score;
        resetStatus(0);
    }

    /// alternative constructor for a pattern with only one color
    public TilePatternObjective(Color color, List<Coord> pattern, int score) {
        this(Collections.nCopies(pattern.size(), color), pattern, score);
    }

    public TilePatternObjective(Color color, List<Coord> pattern) {
        this(Collections.nCopies(pattern.size(), color), pattern, 1);
    }

    private void resetStatus(int completion) {
        var patternSize = patternVariations.stream().findAny().orElseThrow().size();
        status = new Status(completion, patternSize);
    }

    /// Generate all possible rotations of a pattern
    private List<List<Element>> generateRotations(List<Element> pattern) {
        var all_rotations = new ArrayList<List<Element>>();
        all_rotations.add(pattern);

        // rotate the pattern to find all possible patterns
        final int ROTATION_COUNT = 6;
        for (int i = 1; i < ROTATION_COUNT; ++i) {
            var current_rotation = new ArrayList<Element>();
            current_rotation.add(pattern.get(0));
            for (int j = 1; j < pattern.size(); ++j) {
                current_rotation.add(
                        all_rotations.get(i - 1).get(j).rotate(current_rotation.get(0).coord()));
            }
            all_rotations.add(current_rotation);
        }
        return all_rotations;
    }

    /// Generate shifted patterns. That is to say, patterns with the same elements, but with
    // different origins
    private List<List<Element>> generateShifts(List<Element> pattern) {
        var all_shifts = new ArrayList<List<Element>>();
        for (int i = 0; i < pattern.size(); ++i) {
            var offset = pattern.get(i).coord().negated();
            var current_shift = pattern.stream().map(e -> e.offset(offset)).toList();
            all_shifts.add(current_shift);
        }
        return all_shifts;
    }

    @Override
    public Status computeAchieved(Board board, Action lastAction, VisibleInventory ignored) {
        // Once the objective is achieved, it stays achieved
        if (status.achieved()) {
            return status;
        }
        // We know the status can only change if the last action was a tile placement
        if (!(lastAction instanceof Action.PlaceTile placeTile)) {
            return status;
        }
        // Test all possible patterns
        var coord = placeTile.coord();
        status =
                patternVariations.stream()
                        .map(pattern -> isPatternAt(board, coord, pattern))
                        .max(Comparator.comparingDouble(Status::progressFraction))
                        .orElseThrow();
        return status;
    }

    @Override
    public Status status() {
        return status;
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

    private Status isPatternAt(Board board, Coord coord, List<Element> pattern) {
        var matchCount =
                pattern.stream()
                        .map(el -> el.offset(coord))
                        .filter(el -> isValidTile(board, el))
                        .count();
        return new Status((int) matchCount, pattern.size());
    }
}
