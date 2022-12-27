package takenoko;

public class ActionValidator {
    private final Board board;
    private final TileDeck deck;
    private final int irrigationStickCount;

    public ActionValidator(Board board, TileDeck deck, int irrigationStickCount) {
        this.board = board;
        this.deck = deck;
        this.irrigationStickCount = irrigationStickCount;
    }

    public boolean isValid(Action action) {
        return switch (action) {
            case Action.None ignored -> true;
            case Action.PlaceIrrigationStick a -> isValid(a);
            case Action.PlaceTile a -> isValid(a);
            case Action.TakeIrrigationStick a -> isValid(a);
            case Action.UnveilObjective a -> isValid(a);
            case Action.MoveGardener a -> isValid(a);
            case Action.MovePanda a -> isValid(a);
        };
    }

    private boolean isValid(Action.PlaceIrrigationStick action) {
        var coord = action.coord();
        var side = action.side();

        try {
            var tile = board.getTile(coord);
            return !tile.isSideIrrigated(side);
        } catch (BoardException e) {
            return false;
        }
    }

    private boolean isValid(Action.PlaceTile action) {
        return deck.size() > 0 && board.getAvailableCoords().contains(action.coord());
    }

    private boolean isValid(Action.TakeIrrigationStick action) {
        return irrigationStickCount > 0;
    }

    private boolean isValid(Action.UnveilObjective action) {
        return action.objective().wasAchievedAfterLastCheck();
    }

    private boolean isValid(Action.MoveGardener action) {
        return board.getPlacedCoords().contains(action.coord())
                && board.getGardenerCoord().isAlignedWith(action.coord());
    }

    private boolean isValid(Action.MovePanda action) {
        return board.getPlacedCoords().contains(action.coord())
                && board.getPandaCoord().isAlignedWith(action.coord());
    }
}
