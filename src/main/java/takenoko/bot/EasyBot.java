package takenoko.bot;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import takenoko.Action;
import takenoko.BambooTile;
import takenoko.Board;
import takenoko.Coord;
import takenoko.utils.Pair;

/** A bot that chooses actions randomly. */
public class EasyBot extends DefaultBot {

    @Override
    public Pair<Action, Action> chooseActions(Board board) {
        Set<Coord> availableCoords = board.getAvailableCoords();

        BambooTile bambooTile1 =
                new BambooTile(); // Milestone 1... While we do not have a stack of tiles
        BambooTile bambooTile2 =
                new BambooTile(); // Milestone 1... While we do not have a stack of tiles
        // Note for me later: once the stack is implemented, handle the case where it can be empty
        // so Action.NONE will be used

        Coord coord1 = chooseRandom(availableCoords);
        // Coord2 must be different from Coord1 AND can (or not) be adjacent to Coord1
        availableCoords.remove(coord1);
        availableCoords.addAll(List.of(coord1.adjacentCoords()));
        Coord coord2 = chooseRandom(availableCoords);

        return Pair.of(
                new Action.PlaceTile(coord1, bambooTile1),
                new Action.PlaceTile(coord2, bambooTile2));
    }

    private Coord chooseRandom(Set<Coord> coords) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, coords.size());
        return coords.toArray(new Coord[0])[randomIndex];
    }
}
