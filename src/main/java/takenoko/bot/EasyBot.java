package takenoko.bot;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import takenoko.*;

/** A bot that chooses actions randomly. */
public class EasyBot extends PlayerBase<EasyBot> implements PlayerBase.PlayerBaseInterface {

    public Action chooseActionImpl(Board board) {
        Set<Coord> availableCoords = board.getAvailableCoords();

        BambooTile bambooTile =
                new BambooTile(); // Milestone 1... While we do not have a stack of tiles
        // Note for me later: once the stack is implemented, handle the case where it can be empty
        // so Action.NONE will be used

        Coord coord = chooseRandom(availableCoords);

        return new Action.PlaceTile(coord, bambooTile);
    }

    private Coord chooseRandom(Set<Coord> coords) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, coords.size());
        return coords.toArray(new Coord[0])[randomIndex];
    }
}
